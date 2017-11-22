package com.theeye.api.v1.chess.analysis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.*;
import static org.opencv.core.CvType.CV_16S;

@AllArgsConstructor(staticName = "of")
public class MatProcessor {

     private static final int CANNY_LOW_THRESHOLD = 65;
     private static final double CANNY_RATIO = 5.0;
     private static final int GAUSSIAN_WIDTH = 3;
     private static final int GAUSSIAN_HEIGHT = 3;
     private static final int SOBEL_KSIZE = 3;
     private static final int SOBEL_DELTA = 0;
     private static final int SOBEL_SCALE = 1;
     private static final int SOBEL_DDEPTH = CV_16S;

     @Getter
     private Mat mat;

     public static MatProcessor ofClone(Mat mat) {
          return new MatProcessor(mat.clone());
     }

     @NotNull
     public MatProcessor applyCannyEdgeDetection() {
          Mat temp = new Mat();
          Imgproc.Canny(mat, temp, CANNY_LOW_THRESHOLD, CANNY_LOW_THRESHOLD * CANNY_RATIO);
          this.mat = temp;
          return this;
     }

     @NotNull
     public MatProcessor applyCannyEdgeDetection(int lowTreshold, double ratio) {
          Mat temp = new Mat();
          Imgproc.Canny(mat, temp, lowTreshold, lowTreshold * ratio);
          this.mat = temp;
          return this;
     }

     @NotNull
     public MatProcessor applySobelDerivatives() {
          Mat gradX = new Mat();
          Mat gradY = new Mat();
          Mat absoluteGradX = new Mat();
          Mat absoluteGradY = new Mat();
          Imgproc.Sobel(mat, gradX, SOBEL_DDEPTH, 1, 0, SOBEL_KSIZE, SOBEL_SCALE, SOBEL_DELTA, BORDER_DEFAULT);
          Imgproc.Sobel(mat, gradY, SOBEL_DDEPTH, 0, 1, SOBEL_KSIZE, SOBEL_SCALE, SOBEL_DELTA, BORDER_DEFAULT);
          convertScaleAbs(gradX, absoluteGradX);
          convertScaleAbs(gradY, absoluteGradY);
          addWeighted(absoluteGradX, 0.5, absoluteGradY, 0.5, 0, mat);
          return this;
     }

     @NotNull
     public MatProcessor applyGaussianBlur() {
          Imgproc.GaussianBlur(mat, mat, new Size(GAUSSIAN_WIDTH, GAUSSIAN_HEIGHT), 0, 0, BORDER_DEFAULT);
          return this;
     }

     @NotNull
     public MatProcessor applyGreyscale() {
          Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
          return this;
     }
}
