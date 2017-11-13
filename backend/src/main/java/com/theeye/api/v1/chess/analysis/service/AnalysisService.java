package com.theeye.api.v1.chess.analysis.service;

import org.jetbrains.annotations.NotNull;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.core.CvType.CV_16S;

@Service
public class AnalysisService {

     public void determinePositions(Mat image) {
          MatOfPoint2f matOfPoint2f = detectChessboardCorners(image);
     }

     public MatOfPoint2f detectChessboardCorners(Mat image) {
          MatOfPoint2f points = new MatOfPoint2f();
          boolean patternFound = Calib3d.findChessboardCorners(image, image.size(), points);
          Mat mat = new Mat();
          image.copyTo(mat);
          Calib3d.drawChessboardCorners(mat, new Size(10.0, 10.0), points, patternFound);
          return points;
     }

     public Mat detectLines(Mat image) {
          Mat mat = new Mat();
          image.copyTo(mat);

          Imgproc.GaussianBlur( mat, mat, new Size(3,3), 0, 0, BORDER_DEFAULT );

          Mat gray = new Mat();
          Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);

          Mat grad = applySobel(gray);

          Mat edges = applyCannyEdgeDetection(grad);

          Mat lines = new Mat();
          Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 70, 100, 15);

          return lines;
     }

     @NotNull
     private Mat applyCannyEdgeDetection(Mat grad) {
          Mat edges = new Mat();
          int lowThreshold = 65;
          int ratio = 5;
          Imgproc.Canny(grad, edges, lowThreshold, lowThreshold * ratio);
          return edges;
     }

     @NotNull
     private Mat applySobel(Mat gray) {
          int scale = 1;
          int delta = 0;
          int ddepth = CV_16S;
          Mat grad_x = new Mat(), grad_y = new Mat();
          Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
          Imgproc.Sobel( gray, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT );
          Imgproc.Sobel( gray, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT );
          convertScaleAbs( grad_x, abs_grad_x );
          convertScaleAbs( grad_y, abs_grad_y );
          Mat grad = new Mat();
          addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );
          return grad;
     }
}
