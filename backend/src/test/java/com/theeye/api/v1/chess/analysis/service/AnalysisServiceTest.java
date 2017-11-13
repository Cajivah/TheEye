package com.theeye.api.v1.chess.analysis.service;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opencv.calib3d.Calib3d.*;
import static org.opencv.core.Core.BORDER_DEFAULT;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.core.CvType.CV_16S;

class AnalysisServiceTest {

     private AnalysisService sut = new AnalysisService();

     static {
          System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     }


     @Test
     void test() throws IOException {


//          Resource resource = new ClassPathResource("startingSetUp/test.png");
//          Resource resource = new ClassPathResource("startingSetUp/big_blackwhite_brown_webcam.jpg");
          Resource resource = new ClassPathResource("startingSetUp/small_blackgrey_brown_webcam.jpg");
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();

          Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

          Mat grey = new Mat(mat.height(), mat.width(), mat.type());
          Imgproc.cvtColor(mat, grey, Imgproc.COLOR_BGR2GRAY);

          Mat trim = trim(mat);

//          Mat mat = Imgcodecs.imread("src/test/resources/startingSetUp/big_blackwhite_brown_webcam.jpg");
          MatOfPoint2f matOfPoint = new MatOfPoint2f();
          Size size = new Size(7, 7);
          boolean patternFound = Calib3d.findChessboardCorners(grey, size, matOfPoint,
                  CALIB_CB_ADAPTIVE_THRESH);
          Calib3d.drawChessboardCorners(grey, size, matOfPoint, patternFound);

          saveToFile(trim, "TERAZ-BLAGAM2");
          //assertTrue(patternFound);
     }

     private Mat trim(Mat mat) throws IOException {
          Mat gray = new Mat();
          Imgproc.GaussianBlur( mat, mat, new Size(3,3), 0, 0, BORDER_DEFAULT );
          Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);

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



          // detect the edges
          Mat edges = new Mat();
          int lowThreshold = 65;
          int ratio = 5;
          Imgproc.Canny(grad, edges, lowThreshold, lowThreshold * ratio);

          Mat lines = new Mat();
          Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 70, 100, 15);
          System.out.println(lines.rows());
          for(int i = 0; i < lines.rows(); i++) {
               double[] val = lines.get(i, 0);
               Imgproc.line(mat, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
          }
          return mat;
     }


     public void saveToFile(Mat mat, String filename) throws IOException {
          MatOfByte mob = new MatOfByte();
          Imgcodecs.imencode(".jpg", mat, mob);
          byte[] ba = mob.toArray();
          BufferedImage save = ImageIO.read(new ByteArrayInputStream(ba));
          File outputfile = new File(filename + ".jpg");
          ImageIO.write(save, "jpg", outputfile);
     }
}