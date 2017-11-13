package com.theeye.api.v1.chess.analysis.service;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Vec2d;
import com.theeye.api.v1.chess.analysis.exception.PatternNotFoundException;
import com.theeye.api.v1.chess.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.analysis.util.MatWorker;
import org.jetbrains.annotations.NotNull;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class AnalysisService {

     public static final int CORNERS_PATTERN_WIDTH = 7;
     public static final int CORNERS_PATTERN_HEIGHT = 7;

     public static final int TOP_CORNER = 0;
     public static final int RIGHT_CORNER = 0;
     public static final int BOTTOM_CORNER = 0;
     public static final int LEFT_CORNER = 0;

     private final LineMapper lineMapper;

     @Autowired
     public AnalysisService(LineMapper lineMapper) {
          this.lineMapper = lineMapper;
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
          Point[] corners = initializeCornerPoints();
          convertToParametrized(lines);
          return corners;
     }

     private Vec2d convertToParametrized(Mat lines) {
          LinkedList<ParametrizedLine2D> parametrizedLines = new LinkedList<>();
          for(int i = 0; i < lines.rows(); i++) {
               double[] points = lines.get(i, 0);
               ParametrizedLine2D line = lineMapper.toParametrizedLine(points);
               parametrizedLines.add(line);
          }
          return null;
     }

     private Point[] initializeCornerPoints() {
          Point[] corners = new Point[4];
          for(int i = 0; i < corners.length; ++i) {
               corners[i] = new Point();
          }
          return corners;
     }

}
