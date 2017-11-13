package com.theeye.api.v1.chess.analysis.service;

import com.sun.javafx.geom.Point2D;
import com.theeye.api.v1.chess.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.analysis.util.MatWorker;
import com.theeye.api.v1.chess.analysis.util.ParametrizedLineWorker;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_HEIGHT;
import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_WIDTH;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalysisServiceTest {

     private LineMapper lineMapper = new LineMapper();

     private AnalysisService sut = new AnalysisService(lineMapper);

     static {
          System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     }

     Mat mat;

     List<String> filenames = Lists.newArrayList(
             "startingSetUp/big_blackwhite_brown_webcam.jpg", //0
             "startingSetUp/printed_blackwhite_brown_webcam.jpg", //1
             "startingSetUp/printed_blackwhite_brown_webcam2.jpg", //2
             "startingSetUp/printed_blackwhite_brown_webcam3.jpg", //3
             "startingSetUp/printed_blackwhite_empty_webcam.jpg", //4
             "startingSetUp/printed_blackwhite_empty_webcam_white-back.jpg", //5
             "startingSetUp/big_blackwhite_brown_webcam.jpg", //6
             "startingSetUp/big_blackwhite_empty_webcam.jpg",  //7
             "startingSetUp/big_blackwhite_brown_webcam_downscaled2.jpg"  //8
     );

     @BeforeEach
     public void setUp() throws IOException {
          Resource resource = new ClassPathResource(filenames.get(1));
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();
          mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
     }

     @Test
     void tileCornerDetectionTrimmed() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          MatOfPoint2f tileCorners = new MatOfPoint2f();
          boolean patternFound = sut.detectTilesCorners(mat, tileCorners);
          Calib3d.drawChessboardCorners(mat, new Size(CORNERS_PATTERN_HEIGHT, CORNERS_PATTERN_WIDTH), tileCorners, true);
          assertTrue(patternFound);
          saveToFile(mat, "InnerCornerDetection");
     }

     @Test
     void floatingTest() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);

          Mat mat = new Mat();
          Core.copyMakeBorder(trimmed, mat, 100, 100, 100, 100, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));
          MatOfPoint2f tileCorners = new MatOfPoint2f();
          boolean patternFound = sut.detectTilesCorners(mat, tileCorners);
          Calib3d.drawChessboardCorners(mat, new Size(CORNERS_PATTERN_HEIGHT, CORNERS_PATTERN_WIDTH), tileCorners, true);

          saveToFile(mat, "TrimToRoi");
     }

     @Test
     void test() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          List<ParametrizedLine2D> parametrizedLines = lineMapper.toParametrizedLines(lines);
          Point2D horizontalCenter = new Point2D(mat.width() / 2, 0);
          List<ParametrizedLine2D> horizontalBounds =
                  ParametrizedLineWorker.of(parametrizedLines)
                                        .filterHorizontalLines()
                                        .sortByDistance(horizontalCenter)
                                        .firstAndLast()
                                        .getLines();
          Point2D verticalCenter = new Point2D(0, mat.height() / 2);
          List<ParametrizedLine2D> verticalBounds =
                  ParametrizedLineWorker.of(parametrizedLines)
                                        .filterVerticalLines()
                                        .sortByDistance(verticalCenter)
                                        .firstAndLast()
                                        .getLines();
          ParametrizedLine2D bound = horizontalBounds.get(0);
          Imgproc.line(mat, new Point(bound.x1, bound.y1), new Point(bound.x2, bound.y2), new Scalar(0, 0, 255), 2);
          bound = horizontalBounds.get(1);
          Imgproc.line(mat, new Point(bound.x1, bound.y1), new Point(bound.x2, bound.y2), new Scalar(0, 255, 255), 2);
          bound = verticalBounds.get(0);
          Imgproc.line(mat, new Point(bound.x1, bound.y1), new Point(bound.x2, bound.y2), new Scalar(0, 255,0), 2);
          bound = verticalBounds.get(1);
          Imgproc.line(mat, new Point(bound.x1, bound.y1), new Point(bound.x2, bound.y2), new Scalar(255, 255, 0), 2);

          saveToFile(mat, "bounds");
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
          saveToFile(mat, "LineDetectionFull");
     }

     @Test
     void bounds() throws IOException {
          Mat lines = sut.detectLines(mat);
          Point[] corners = sut.findCorners(mat, lines);
          for(int i = 0; i < corners.length; i++) {
//               Imgproc.line(mat, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
               Imgproc.circle(mat, corners[i], 5, new Scalar(0, 0, 255), 3);
          }
          assertNotNull(lines);
          assertTrue(lines.rows() > 0);
          saveToFile(mat, "CornersDetection");
     }

     public void saveToFile(Mat mat, String filename) throws IOException {
          MatOfByte mob = new MatOfByte();
          Imgcodecs.imencode(".jpg", mat, mob);
          byte[] ba = mob.toArray();
          BufferedImage save = ImageIO.read(new ByteArrayInputStream(ba));
          File outputfile = new File("test-results/" + filename + ".jpg");
          ImageIO.write(save, "jpg", outputfile);
     }
}