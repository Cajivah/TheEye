package com.theeye.api.v1.chess.image.analysis.service;

import com.sun.javafx.geom.Point2D;
import com.theeye.api.v1.chess.image.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.image.analysis.mapper.LineMapper;
import com.theeye.api.v1.chess.image.analysis.model.domain.ParametrizedLine2D;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.image.analysis.service.color.ColorAnalysisService;
import com.theeye.api.v1.chess.image.analysis.service.position.TileCornersService;
import com.theeye.api.v1.chess.image.analysis.util.CoordUtil;
import com.theeye.api.v1.chess.image.analysis.util.MatProcessor;
import com.theeye.api.v1.chess.image.analysis.util.ParametrizedLineProcessor;
import com.theeye.api.v1.chess.image.analysis.util.TileScaler;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.theeye.api.v1.chess.image.analysis.service.AnalysisService.CORNERS_PATTERN_HEIGHT;
import static com.theeye.api.v1.chess.image.analysis.service.AnalysisService.CORNERS_PATTERN_WIDTH;
import static com.theeye.api.v1.common.util.SaveToFile.save;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThesisVisualisations {


     private CoordsMapper coordsMapper = new CoordsMapper();
     private LineMapper lineMapper = new LineMapper();
     private TileCornersService tileCornersService = new TileCornersService();
     private ColorAnalysisService colorAnalysisService = new ColorAnalysisService();

     private AnalysisService sut =
             new AnalysisService(
                     lineMapper,
                     tileCornersService,
                     colorAnalysisService);

     static {
          System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     }

     Mat mat;

     List<String> filenames = Lists.newArrayList(
             "startingSetUp/1.jpg",
             "startingSetUp/2.jpg",
             "startingSetUp/3.jpg",
             "startingSetUp/4.jpg",
             "startingSetUp/5.jpg"
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
          save(trimmed, "trim");
     }

     @Test
     void canny() throws IOException {
          Mat canny = MatProcessor
                  .ofClone(mat)
                  .applyGaussianBlur()
                  .applyCannyEdgeDetection()
                  .getMat();
          save(canny, "canny");
     }

     @Test
     void gaussian() throws IOException {
          Mat gaussian = MatProcessor.ofClone(this.mat)
                                  .applyGaussianBlur()
                                  .getMat();
          save(gaussian, "gaussian");
     }

     @Test
     void innerCorners() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          Point[][] points = sut.detectAllTilesCornerPoints(trimmed);
          Arrays.stream(points).flatMap(Arrays::stream).forEach(point -> drawCircles(trimmed, point));
          save(trimmed, "innerCorners");
     }

     private void drawCircles(Mat trimmed, Point p) {
          Imgproc.circle(trimmed, p, 5, new Scalar(0, 0, 255), 3);
     }

     @Test
     void lines() throws IOException {
          Mat lines = sut.detectLines(mat);
          for(int i = 0; i < lines.rows(); i++) {
               double[] val = lines.get(i, 0);
               Imgproc.line(mat, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
          }
          save(mat, "AllLines");
     }

     @Test
     void boundsAndCorners() throws IOException {
          Mat lines = sut.detectLines(mat);
          Point[] corners = sut.findCorners(mat, lines);
          for(int i = 0; i < corners.length; i++) {
               Imgproc.circle(mat, corners[i], 5, new Scalar(0, 0, 255), 3);
          }
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
          Imgproc.line(this.mat, new Point(verticalBounds.get(1).x1, verticalBounds.get(1).y1), new Point(verticalBounds.get(1).x2, verticalBounds.get(1).y2), new Scalar(0, 0, 255), 2);
          Imgproc.line(this.mat, new Point(horizontalBounds.get(0).x1, horizontalBounds.get(0).y1), new Point(horizontalBounds.get(0).x2, horizontalBounds.get(0).y2), new Scalar(0, 0, 255), 2);
          Imgproc.line(this.mat, new Point(horizontalBounds.get(1).x1, horizontalBounds.get(1).y1), new Point(horizontalBounds.get(1).x2, horizontalBounds.get(1).y2), new Scalar(0, 0, 255), 2);

          save(this.mat, "boundsAndCorners");
     }

     @Test
     void innerRoi() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          Point[][] points = sut.detectAllTilesCornerPoints(trimmed);
          TileCorners[][] tileCorners = coordsMapper.toTilesCoords(points);
          Resource resource = new ClassPathResource(filenames.get(1));
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();
          mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
          Mat trimmedFinal = sut.trimToCorners(this.mat, corners);
          for (int i = 0; i < 8; i++) {
               for (int j = 0; j < 8; j++) {
                    TileCorners roiCorners = limitRoi(tileCorners[i][j], 0.25);
                    Rect rect = CoordUtil.innerRect(roiCorners);
                    Imgproc.rectangle(trimmedFinal, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(0, 0, 255), 2);
               }
          }
          save(trimmedFinal, "color-roi");
     }
     public TileCorners limitRoi(TileCorners tile, double areaScale) {
          double sideScale = Math.pow(areaScale, 0.5);
          return TileScaler.computeScaledTile(tile, sideScale);
     }

     @Test
     void move() throws IOException {
          Mat lines = sut.detectLines(this.mat);
          Point[] corners = sut.findCorners(mat, lines);
          Mat trimmed = sut.trimToCorners(this.mat, corners);
          Point[][] points = sut.detectAllTilesCornerPoints(trimmed);
          TileCorners[][] tileCorners = coordsMapper.toTilesCoords(points);
          Resource resource = new ClassPathResource(filenames.get(2));
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();
          mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
          Mat trimmedFinal = sut.trimToCorners(this.mat, corners);

                    TileCorners roiCorners = limitRoi(tileCorners[4][1], 0.25);
                    Rect rect = CoordUtil.innerRect(roiCorners);
                    Imgproc.rectangle(trimmedFinal, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(0, 0, 255), 2);

          roiCorners = limitRoi(tileCorners[4][3], 0.25);
          rect = CoordUtil.innerRect(roiCorners);
          Imgproc.rectangle(trimmedFinal, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(0, 0, 255), 2);

          save(trimmedFinal, "move");
     }

}
