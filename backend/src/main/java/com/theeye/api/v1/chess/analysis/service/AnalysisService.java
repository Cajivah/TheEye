package com.theeye.api.v1.chess.analysis.service;

import com.theeye.api.v1.chess.analysis.exception.PatternNotFoundException;
import com.theeye.api.v1.chess.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.analysis.util.LineUtils;
import com.theeye.api.v1.chess.analysis.util.MatWorker;
import org.jetbrains.annotations.NotNull;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {

     public static final int CORNERS_PATTERN_WIDTH = 7;
     public static final int CORNERS_PATTERN_HEIGHT = 7;

     public static final int TOP_RIGHT_CORNER = 0;
     public static final int BOTTOM_RIGHT_CORNER = 1;
     public static final int BOTTOM_LEFT_CORNER = 2;
     public static final int TOP_LEFT_CORNER = 3;

     private final LineMapper lineMapper;
     private final LineUtils lineUtils;

     @Autowired
     public AnalysisService(LineMapper lineMapper,
                            LineUtils lineUtils) {
          this.lineMapper = lineMapper;
          this.lineUtils = lineUtils;
     }


     public void determinePositions(Mat image) {
          MatOfPoint2f tilesCorners = new MatOfPoint2f();
          boolean patternFound = detectTilesCorners(image, tilesCorners);
     }

     public boolean detectTilesCorners(Mat image, MatOfPoint2f corners) {
          boolean patternFound = Calib3d.findChessboardCorners(image, image.size(), corners);
          if(!patternFound) {
               throw new PatternNotFoundException();
          }
          return patternFound;
     }

     @NotNull
     public Mat detectLines(Mat image) {
          Mat preprocessedImage =
                  MatWorker.ofClone(image)
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
     public Point[] findCorners(Mat lines) {
          List<ParametrizedLine2D> parametrizedLines = lineMapper.toParametrizedLines(lines);
          Point[] corners = initializeCornerPoints();
          List<ParametrizedLine2D> horizontals = lineUtils.filterHorizontalLines(parametrizedLines);
          List<ParametrizedLine2D> verticals = lineUtils.filterVerticalLines(parametrizedLines);
          ParametrizedLine2D topBound = getFirstLine(horizontals);
          ParametrizedLine2D bottomBound = getLastLine(horizontals);
          ParametrizedLine2D rightBound = getFirstLine(verticals);
          ParametrizedLine2D leftBound = getLastLine(verticals);

          return corners;
     }

     private ParametrizedLine2D getFirstLine(List<ParametrizedLine2D> lines) {
          return lines.get(0);
     }

     private ParametrizedLine2D getLastLine(List<ParametrizedLine2D> lines) {
          return lines.get(lines.size() - 1);
     }

     private Point[] initializeCornerPoints() {
          Point[] corners = new Point[4];
          for(int i = 0; i < corners.length; ++i) {
               corners[i] = new Point();
          }
          return corners;
     }

}
