package com.theeye.api.v1.chess.analysis.service;

import com.theeye.api.v1.chess.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_HEIGHT;
import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_WIDTH;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalysisServiceTest {

     private LineMapper lineMapper = new LineMapper();
     private CoordsMapper coordsMapper = new CoordsMapper();
     private TileCornersService tileCornersService = new TileCornersService();

     private AnalysisService sut = new AnalysisService(lineMapper, coordsMapper, tileCornersService);

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
          Resource resource = new ClassPathResource(filenames.get(7));
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();
          mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
     }

     @BeforeAll
     private static void setUpAll() throws IOException {
          Path path = Paths.get("test-results");
          if(!Files.exists(path)) {
               Files.createDirectory(path);
          }
     }

     @Test
     void tileCornerDetectionTrimmed() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          MatOfPoint2f tileCorners = sut.detectInnerTilesCorners(mat);
          Calib3d.drawChessboardCorners(mat, new Size(CORNERS_PATTERN_HEIGHT, CORNERS_PATTERN_WIDTH), tileCorners, true);
          saveToFile(mat, "InnerCornerDetection");
     }

     @Test
     void floatingTest() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);

          saveToFile(trimmed, "TrimToRoi");
     }

     @Test
     void test() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          Point[][] points = sut.detectAllTilesCornerPoints(trimmed);
          Arrays.stream(points).flatMap(Arrays::stream).forEach(point -> drawCircles(trimmed, point));
          saveToFile(trimmed, "test");
     }

     private void drawCircles(Mat trimmed, Point p) {
          Imgproc.circle(trimmed, p, 5, new Scalar(0, 0, 255), 3);
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