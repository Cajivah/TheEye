package com.theeye.api.v1.chess.analysis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.core.CvType.CV_16S;

@AllArgsConstructor(staticName = "of")
public class MatWorker {

     private static final int CANNY_LOW_THRESHOLD = 65;
     private static final int CANNY_RATIO = 5;
     private static final int GAUSSIAN_WIDTH = 3;
     private static final int GAUSSIAN_HEIGHT = 3;
     private static final int SOBEL_KSIZE = 3;
     private static final int SOBEL_DELTA = 0;
     private static final int SOBEL_SCALE = 1;
     private static final int SOBEL_DDEPTH = CV_16S;

     @Getter
     private Mat mat;

     public static MatWorker ofClone(Mat mat) {
          return new MatWorker(mat.clone());
     }

     @NotNull
     public MatWorker applyCannyEdgeDetection() {
          Imgproc.Canny(mat, mat, CANNY_LOW_THRESHOLD, CANNY_LOW_THRESHOLD * CANNY_RATIO);
          return this;
     }

     @NotNull
     public MatWorker applyCannyEdgeDetection(int lowTreshold, int ratio) {
          Imgproc.Canny(mat, mat, lowTreshold, lowTreshold * ratio);
          return this;
     }

     @NotNull
     public MatWorker applySobelDerivatives() {
          Mat grad_x = new Mat(), grad_y = new Mat();
          Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
          Imgproc.Sobel(mat, grad_x, SOBEL_DDEPTH, 1, 0, SOBEL_KSIZE, SOBEL_SCALE, SOBEL_DELTA, BORDER_DEFAULT );
          Imgproc.Sobel(mat, grad_y, SOBEL_DDEPTH, 0, 1, SOBEL_KSIZE, SOBEL_SCALE, SOBEL_DELTA, BORDER_DEFAULT );
          convertScaleAbs( grad_x, abs_grad_x );
          convertScaleAbs( grad_y, abs_grad_y );
          addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, mat);
          return this;
     }

     @NotNull
     public MatWorker applyGaussianBlur() {
          Imgproc.GaussianBlur(mat, mat, new Size(GAUSSIAN_WIDTH,GAUSSIAN_HEIGHT), 0, 0, BORDER_DEFAULT );
          return this;
     }

     @NotNull
     public MatWorker applyGreyscale() {
          Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
          return this;
     }
}
