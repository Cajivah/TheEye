package com.theeye.api.v1.chess.image.analysis.service;

import com.sun.javafx.geom.Point2D;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.image.analysis.exception.PatternNotFoundException;
import com.theeye.api.v1.chess.image.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.image.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.image.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.image.analysis.service.color.ColorAnalysisService;
import com.theeye.api.v1.chess.image.analysis.service.position.TileCornersService;
import com.theeye.api.v1.chess.image.analysis.util.MatProcessor;
import com.theeye.api.v1.chess.image.analysis.util.ParametrizedLineProcessor;
import com.theeye.api.v1.common.util.SaveToFile;
import org.jetbrains.annotations.NotNull;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.theeye.api.v1.chess.image.analysis.util.CoordUtil.findDistance3D;
import static com.theeye.api.v1.chess.image.analysis.util.ParametrizedLineProcessor.CLOSER_BOUND;
import static com.theeye.api.v1.chess.image.analysis.util.ParametrizedLineProcessor.FURTHER_BOUND;
import static com.theeye.api.v1.common.util.ComparisionUtil.getBigger;
import static com.theeye.api.v1.common.util.ComparisionUtil.getSmaller;
import static org.opencv.calib3d.Calib3d.CALIB_CB_ADAPTIVE_THRESH;

@Service
public class AnalysisService {

     public static final int CORNERS_PATTERN_WIDTH = 7;
     public static final int CORNERS_PATTERN_HEIGHT = 7;

     public static final int TOP_RIGHT_CORNER = 0;
     public static final int BOTTOM_RIGHT_CORNER = 1;
     public static final int BOTTOM_LEFT_CORNER = 2;
     public static final int TOP_LEFT_CORNER = 3;

     public static final int X_AXIS_FLIP_CODE = 0;

     private final LineMapper lineMapper;
     private final TileCornersService tileCornersService;
     private final ColorAnalysisService colorAnalysisService;

     @Autowired
     public AnalysisService(LineMapper lineMapper,
                            TileCornersService tileCornersService,
                            ColorAnalysisService colorAnalysisService) {
          this.lineMapper = lineMapper;
          this.tileCornersService = tileCornersService;
          this.colorAnalysisService = colorAnalysisService;
     }

     public Point[][] detectAllTilesCornerPoints(Mat image) {
          MatOfPoint2f innerCorners = detectInnerTilesCorners(image);
          return tileCornersService.findMissingBorderCorners(innerCorners);
     }

     public MatOfPoint2f detectInnerTilesCorners(Mat image) {
          MatOfPoint2f innerCorners = new MatOfPoint2f();
          boolean patternFound = Calib3d.findChessboardCorners(
                  image,
                  new Size(CORNERS_PATTERN_WIDTH, CORNERS_PATTERN_HEIGHT),
                  innerCorners,
                  CALIB_CB_ADAPTIVE_THRESH);
          if (!patternFound) {
               throw new PatternNotFoundException();
          }
          return innerCorners;
     }

     @NotNull
     public Mat detectLines(Mat image) {
          Mat preprocessedImage =
                  MatProcessor.ofClone(image)
                              .applyGaussianBlur()
                              .applyCannyEdgeDetection()
                              .getMat();

          Mat lines = new Mat();
          Imgproc.HoughLinesP(preprocessedImage, lines, 1, Math.PI / 180, 70, 100, 15);

          return lines;
     }

     @NotNull
     public Point[] findCorners(Mat image, Mat lines) {
          List<ParametrizedLine2D> parametrizedLines = lineMapper.toParametrizedLines(lines);
          Point2D horizontalCenter = new Point2D(image.width() / 2, 0);
          List<ParametrizedLine2D> horizontalBounds =
                  ParametrizedLineProcessor.of(parametrizedLines)
                                           .filterHorizontalLines()
                                           .sortByDistance(horizontalCenter)
                                           .firstAndLast()
                                           .getLines();
          Point2D verticalCenter = new Point2D(0, image.height() / 2);
          List<ParametrizedLine2D> verticalBounds =
                  ParametrizedLineProcessor.of(parametrizedLines)
                                           .filterVerticalLines()
                                           .sortByDistance(verticalCenter)
                                           .firstAndLast()
                                           .getLines();
          return getBoundsIntersections(horizontalBounds, verticalBounds);
     }

     @NotNull
     private Point[] getBoundsIntersections(List<ParametrizedLine2D> horizontalBounds,
                                            List<ParametrizedLine2D> verticalBounds) {
          ParametrizedLine2D bottomBound = horizontalBounds.get(FURTHER_BOUND);
          ParametrizedLine2D topBound = horizontalBounds.get(CLOSER_BOUND);
          ParametrizedLine2D rightBound = verticalBounds.get(FURTHER_BOUND);
          ParametrizedLine2D leftBound = verticalBounds.get(CLOSER_BOUND);

          Point[] corners = new Point[4];
          corners[TOP_RIGHT_CORNER] = topBound.intersect(rightBound);
          corners[BOTTOM_RIGHT_CORNER] = bottomBound.intersect(rightBound);
          corners[BOTTOM_LEFT_CORNER] = bottomBound.intersect(leftBound);
          corners[TOP_LEFT_CORNER] = topBound.intersect(leftBound);
          return corners;
     }

     @NotNull
     public Mat trimToCorners(Mat image, Point[] corners) {
          double minLeft = getSmaller(corners[TOP_LEFT_CORNER].x, corners[BOTTOM_LEFT_CORNER].x);
          double minTop = getSmaller(corners[TOP_LEFT_CORNER].y, corners[TOP_RIGHT_CORNER].y);
          double maxBottom = getBigger(corners[BOTTOM_LEFT_CORNER].y, corners[BOTTOM_RIGHT_CORNER].y);
          double maxRight = getBigger(corners[TOP_RIGHT_CORNER].x, corners[BOTTOM_RIGHT_CORNER].x);
          Point leftTopFrameCorner = new Point(minLeft, minTop);
          Point rightBottomFrameCorner = new Point(maxRight, maxBottom);
          Rect frame = new Rect(leftTopFrameCorner, rightBottomFrameCorner);
          return image.submat(frame);
     }

     public Mat doPreprocessing(Mat src) {
          return rotate(src);
     }

     private Mat rotate(Mat src) {
          Mat rotated = new Mat();
          Core.transpose(src, rotated);
          Core.flip(rotated, rotated, X_AXIS_FLIP_CODE);
          return rotated;
     }

     public ReferenceColors getReferenceColors(Mat preparedImage, TileCorners[][] tilesCorners) {
          return ReferenceColors.builder()
                                .whiteTiles(colorAnalysisService.getWhiteTilesAverages(preparedImage, tilesCorners))
                                .blackTiles(colorAnalysisService.getBlackTilesAverages(preparedImage, tilesCorners))
                                .build();
     }

     public Occupancy[][] getChessboardOccupancy(UnresolvedMove unresolvedMove) {
          Mat image = unresolvedMove.getChessboardImage().getImage();
          Mat trimmed = trimToCorners(image, unresolvedMove.getChessboardCorners());
          Mat preprocessed = doPreprocessing(trimmed);
          Scalar[][] tilesColors = colorAnalysisService.getTilesColorsInPlay(preprocessed, unresolvedMove.getTilesCorners());
          return determineOccupancy(tilesColors, unresolvedMove.getReferenceColors());
     }

     private Occupancy[][] determineOccupancy(Scalar[][] tilesColors, ReferenceColors referenceColors) {
          Occupancy[][] occupancies = new Occupancy[BoardConsts.ROWS][BoardConsts.COLUMNS];
          for (int row = 0; row < BoardConsts.ROWS; ++row) {
               Occupancy[] occupancyRow = occupancies[row];
               Scalar[] tilesColorRow = tilesColors[row];
               determineOccupancyForRow(referenceColors, row, occupancyRow, tilesColorRow);
          }
          return occupancies;
     }

     private void determineOccupancyForRow(ReferenceColors referenceColors, int i, Occupancy[] occupancyRow, Scalar[] tilesColorRow) {
          for (int j = 0; j < BoardConsts.COLUMNS; ++j) {
               Scalar tileColor = tilesColorRow[j];
               boolean isTileBackgroundBlack = (((i + j) % 2) == 1);
               TileReferenceColors reference =
                       isTileBackgroundBlack
                               ? referenceColors.getBlackTiles()
                               : referenceColors.getWhiteTiles();
               Occupancy occupancy = findOccupationState(tileColor, reference);
               occupancyRow[j] = occupancy;
          }
     }

     private Occupancy findOccupationState(Scalar tileColor, TileReferenceColors reference) {
          double occupiedByWhiteDistance = findDistance3D(tileColor, reference.getOccupiedByWhite());
          double occupiedByBlackDistance = findDistance3D(tileColor, reference.getOccupiedByBlack());
          double unoccupiedDistance = findDistance3D(tileColor, reference.getUnoccupied());
          Occupancy occupancy;
          if (unoccupiedDistance < occupiedByBlackDistance && unoccupiedDistance < occupiedByWhiteDistance) {
               occupancy = Occupancy.UNOCCUPIED;
          } else if (occupiedByWhiteDistance < occupiedByBlackDistance) {
               occupancy = Occupancy.OCCUPIED_BY_WHITE;
          } else {
               occupancy = Occupancy.OCCUPIED_BY_BLACK;
          }
          return occupancy;
     }
}
