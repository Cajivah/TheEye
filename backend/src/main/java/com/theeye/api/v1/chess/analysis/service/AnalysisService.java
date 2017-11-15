package com.theeye.api.v1.chess.analysis.service;

import com.sun.javafx.geom.Point2D;
import com.theeye.api.v1.chess.analysis.exception.PatternNotFoundException;
import com.theeye.api.v1.chess.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.analysis.util.MatProcessor;
import com.theeye.api.v1.chess.analysis.util.ParametrizedLineProcessor;
import org.jetbrains.annotations.NotNull;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.theeye.api.v1.chess.analysis.util.ParametrizedLineProcessor.CLOSER_BOUND;
import static com.theeye.api.v1.chess.analysis.util.ParametrizedLineProcessor.FURTHER_BOUND;
import static com.theeye.common.ComparisionUtil.getBigger;
import static com.theeye.common.ComparisionUtil.getSmaller;
import static org.opencv.calib3d.Calib3d.CALIB_CB_ADAPTIVE_THRESH;
import static org.opencv.calib3d.Calib3d.CALIB_CB_FILTER_QUADS;

@Service
public class AnalysisService {

     public static final int CORNERS_PATTERN_WIDTH = 7;
     public static final int CORNERS_PATTERN_HEIGHT = 7;

     public static final int TOP_RIGHT_CORNER = 0;
     public static final int BOTTOM_RIGHT_CORNER = 1;
     public static final int BOTTOM_LEFT_CORNER = 2;
     public static final int TOP_LEFT_CORNER = 3;

     private final LineMapper lineMapper;
     private final CoordsMapper coordsMapper;
     private final TileCornersService tileCornersService;

     @Autowired
     public AnalysisService(LineMapper lineMapper,
                            CoordsMapper coordsMapper,
                            TileCornersService tileCornersService) {
          this.lineMapper = lineMapper;
          this.coordsMapper = coordsMapper;
          this.tileCornersService = tileCornersService;
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
                  CALIB_CB_ADAPTIVE_THRESH | CALIB_CB_FILTER_QUADS);
          if(!patternFound) {
               throw new PatternNotFoundException();
          }
          return innerCorners;
     }

     @NotNull
     public Mat detectLines(Mat image) {
          Mat preprocessedImage =
                  MatProcessor.ofClone(image)
                              .applyGaussianBlur()
                              .applyGreyscale()
                              .applySobelDerivatives()
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

     public Mat doPreprocessing(Mat src, Point[] chessboardCorners) {
          return trimToCorners(src, chessboardCorners);
     }

     public ReferenceColors getReferenceColors(Mat preparedImage, TileCorners[][] tilesCorners) {
          return null; //todo
     }
}
