package com.theeye.api.v1.chess.analysis.service;

import com.theeye.api.v1.chess.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.analysis.util.LineUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_HEIGHT;
import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_WIDTH;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalysisServiceTest {

     @Mock
     LineMapper lineMapper;

     @Mock
     private LineUtils lineUtils;

     @InjectMocks
     private AnalysisService sut;

     static {
          System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     }

     Mat mat;

     @BeforeEach
     public void setUp() throws IOException {
          Resource resource = new ClassPathResource("startingSetUp/big_blackwhite_brown_webcam2.jpg");
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();
          mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
     }

     @Test
     void test() throws IOException {
          MatOfPoint2f corners = new MatOfPoint2f();
                  sut.detectTilesCorners(mat, corners);
          Calib3d.drawChessboardCorners(mat, new Size(CORNERS_PATTERN_HEIGHT, CORNERS_PATTERN_WIDTH), corners, true);

          saveToFile(mat, "TERAZ-BLAGAM2");
     }

     @Test
     void lineDetection() throws IOException {
          Mat lines = sut.detectLines(mat);
          for(int i = 0; i < lines.rows(); i++) {
               double[] val = lines.get(i, 0);
               Imgproc.line(mat, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
          }
          assertNotNull(lines);
          assertTrue(lines.rows() > 0);
          saveToFile(mat, "LineDetection");
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