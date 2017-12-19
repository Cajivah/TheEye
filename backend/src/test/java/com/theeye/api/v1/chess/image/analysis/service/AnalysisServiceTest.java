package com.theeye.api.v1.chess.image.analysis.service;

import com.sun.javafx.geom.Point2D;
import com.theeye.api.factory.OccupancyTestFactory;
import com.theeye.api.factory.UnresolvedMoveTestFactory;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.image.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.image.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.image.analysis.service.color.ColorAnalysisService;
import com.theeye.api.v1.chess.image.analysis.service.position.TileCornersService;
import com.theeye.api.v1.chess.image.analysis.util.MatProcessor;
import com.theeye.api.v1.chess.image.analysis.util.ParametrizedLineProcessor;
import extension.NativeLibraryExtension;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import tag.EffectivenessTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.theeye.api.v1.chess.image.analysis.service.AnalysisService.CORNERS_PATTERN_HEIGHT;
import static com.theeye.api.v1.chess.image.analysis.service.AnalysisService.CORNERS_PATTERN_WIDTH;
import static com.theeye.api.v1.common.util.SaveToFile.save;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(NativeLibraryExtension.class)
class AnalysisServiceTest {

     private LineMapper lineMapper = new LineMapper();
     private TileCornersService tileCornersService = new TileCornersService();
     private ColorAnalysisService colorAnalysisService = new ColorAnalysisService();

     private AnalysisService sut =
             new AnalysisService(
                     lineMapper,
                     tileCornersService,
                     colorAnalysisService);
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
             "startingSetUp/big_blackwhite_brown_webcam_downscaled2.jpg",//8
             "startingSetUp/big_blackwhite_brown_webcam2.jpg"  //9
     );

     @BeforeEach
     public void setUp() throws IOException {
          Resource resource = new ClassPathResource(filenames.get(9));
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
     void trimToRoi() throws IOException {
          Mat lines = sut.detectLines(mat);
          Point[] roiCorners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(mat, roiCorners);
          save(trimmed, "TrimToRoi");
     }

     @Test
     void tileCornerDetectionTrimmed() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(this.mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          MatOfPoint2f tileCorners = sut.detectInnerTilesCorners(mat);
          Calib3d.drawChessboardCorners(mat, new Size(CORNERS_PATTERN_HEIGHT, CORNERS_PATTERN_WIDTH), tileCorners, true);
          save(mat, "InnerCornerDetection");
     }

     @Test
     void floatingTest() throws IOException {
          Mat canny = MatProcessor.ofClone(this.mat)
                                  .applyGaussianBlur()
                                  .applyCannyEdgeDetection()
                                  .getMat();
          Mat lines = new Mat();
          Imgproc.HoughLinesP(canny, lines, 1, Math.PI / 180, 40, 80, 15);
          for(int i = 0; i < lines.rows(); i++) {
               double[] val = lines.get(i, 0);
               Imgproc.line(mat, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
          }
          save(mat, "lines");
     }

     @Test
     @Disabled
     void test() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          Point[][] points = sut.detectAllTilesCornerPoints(trimmed);
          Arrays.stream(points).flatMap(Arrays::stream).forEach(point -> drawCircles(trimmed, point));
          save(trimmed, "test");
     }

     @Test
     void test2() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          List<ParametrizedLine2D> parametrizedLines = lineMapper.toParametrizedLines(lines);
          Point2D horizontalCenter = new Point2D(mat.width() / 2, 0);
          List<ParametrizedLine2D> horizontalBounds =
                  ParametrizedLineProcessor.of(parametrizedLines)
                                           .filterHorizontalLines()
                                           .sortByDistance(horizontalCenter)
                                           .firstAndLast()
                                           .getLines();
          Point2D verticalCenter = new Point2D(0, mat.height() / 2);
          List<ParametrizedLine2D> verticalBounds =
                  ParametrizedLineProcessor.of(parametrizedLines)
                                           .filterVerticalLines()
                                           .sortByDistance(verticalCenter)
                                           .firstAndLast()
                                           .getLines();

          Imgproc.line(this.mat, new Point(verticalBounds.get(0).x1, verticalBounds.get(0).y1), new Point(verticalBounds.get(0).x2, verticalBounds.get(0).y2), new Scalar(0, 0, 255), 2);
          Imgproc.line(this.mat, new Point(verticalBounds.get(1).x1, verticalBounds.get(1).y1), new Point(verticalBounds.get(1).x2, verticalBounds.get(1).y2), new Scalar(0, 255, 255), 2);
          Imgproc.line(this.mat, new Point(horizontalBounds.get(0).x1, horizontalBounds.get(0).y1), new Point(horizontalBounds.get(0).x2, horizontalBounds.get(0).y2), new Scalar(255, 0, 255), 2);
          Imgproc.line(this.mat, new Point(horizontalBounds.get(1).x1, horizontalBounds.get(1).y1), new Point(horizontalBounds.get(1).x2, horizontalBounds.get(1).y2), new Scalar(255, 255, 0), 2);

          save(this.mat, "test2");
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
          save(mat, "LineDetectionFull");
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
          save(mat, "CornersDetection");
     }

     @Test
     @EffectivenessTest
     void getChessboardOccupancy() throws IOException {
          UnresolvedMove after1e4 = UnresolvedMoveTestFactory.createAfter1e4();
          Occupancy[][] chessboardOccupancy = sut.getChessboardOccupancy(after1e4);
          assertArrayEquals(chessboardOccupancy, OccupancyTestFactory.createOccupancyAfter1e4());
     }

}