package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.domain.ChessboardImage;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import com.theeye.api.v1.chess.image.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.image.analysis.mapper.ImageMapper;
import com.theeye.api.v1.chess.image.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.dto.PointDTO;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.theeye.api.factory.BoardTestFactory.createInitialBoard;

public class UnresolvedMoveTestFactory {

     private final static ImageMapper imageMapper = new ImageMapper() {
          @Override
          public ChessboardImage toChessboardImage(ChessboardImageDTO chessboardImageDTO) {
               return null;
          }
     };

     private final static CoordsMapper coordsMapper = new CoordsMapper();
     public static UnresolvedMove createAfter1e4() throws IOException {

//          Resource resource = new ClassPathResource("after1e4.txt");
//          String imageBase64 = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());

          Resource resource = new ClassPathResource("startingSetUp/aftere2e4.jpg");
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageBytes));
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bi, "jpg", baos);
          byte[] bytes = baos.toByteArray();
          Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);


          ChessboardImage chessboardImage = ChessboardImage.builder()
                                                           .image(mat)
                                                           .cropped(false)
                                                           .rotated(false)
                                                           .warped(false)
                                                           .build();

          ReferenceColors colors = ReferenceColors.builder()
                                                 .whiteTiles(TileReferenceColors.builder()
                                                                                .unoccupied(new Scalar(228, 209, 203))
                                                                                .occupiedByWhite(new Scalar(83, 121, 141))
                                                                                .occupiedByBlack(new Scalar(28, 29, 29))
                                                                                .build())
                                                 .blackTiles(TileReferenceColors.builder()
                                                                                .unoccupied(new Scalar(67, 64, 64))
                                                                                .occupiedByWhite(new Scalar(79, 118, 139))
                                                                                .occupiedByBlack(new Scalar(18, 18, 18))
                                                                                .build())
                                                  .build();

          PointDTO[][] pointDTOS = new PointDTO[][]{
                  new PointDTO[]{
                          PointDTO.of(4, 5), PointDTO.of(91, 6), PointDTO.of(178, 8),
                          PointDTO.of(263, 9),PointDTO.of(350, 10), PointDTO.of(433, 11),
                          PointDTO.of(519, 12), PointDTO.of(601, 14), PointDTO.of(684, 16)
                  },
                  new PointDTO[]{
                          PointDTO.of(4, 91), PointDTO.of(91, 92), PointDTO.of(177, 93),
                          PointDTO.of(263, 94), PointDTO.of(349, 95), PointDTO.of(434, 96),
                          PointDTO.of(519, 97), PointDTO.of(602, 98), PointDTO.of(685, 99)
                  },
                  new PointDTO[]{
                          PointDTO.of(5, 177), PointDTO.of(91, 178), PointDTO.of(177, 178),
                          PointDTO.of(263, 179), PointDTO.of(349, 180), PointDTO.of(434, 180),
                          PointDTO.of(519, 181), PointDTO.of(603, 182), PointDTO.of(687, 182)
                  },
                  new PointDTO[]{
                          PointDTO.of(3, 263), PointDTO.of(90, 264), PointDTO.of(177, 264),
                          PointDTO.of(262, 265), PointDTO.of(350, 265), PointDTO.of(434, 266),
                          PointDTO.of(520, 266), PointDTO.of(604, 266), PointDTO.of(689, 266)
                  },
                  new PointDTO[]{
                          PointDTO.of(3, 350), PointDTO.of(90,350),PointDTO.of(177,350),
                          PointDTO.of(263,351), PointDTO.of(350,351), PointDTO.of(435,350),
                          PointDTO.of(520,351), PointDTO.of(605,351),PointDTO.of(690,350),
                  },
                  new PointDTO[]{
                          PointDTO.of(2,437), PointDTO.of(89,437), PointDTO.of(177,437),
                          PointDTO.of(263,436),PointDTO.of(350,437), PointDTO.of(435,437),
                          PointDTO.of(520,436),PointDTO.of(605,436), PointDTO.of(690, 437),
                  },
                  new PointDTO[]{
                          PointDTO.of(2,525), PointDTO.of(89,524), PointDTO.of(177,523),
                          PointDTO.of(264,523),PointDTO.of(350,521), PointDTO.of(435, 521),
                          PointDTO.of(521,522), PointDTO.of(606,521), PointDTO.of(691,520),
                  },
                  new PointDTO[]{
                          PointDTO.of(1,612), PointDTO.of(89,611), PointDTO.of(177,611),
                          PointDTO.of(263, 610), PointDTO.of(350, 609), PointDTO.of(436,609),
                          PointDTO.of(522,608), PointDTO.of(607,607), PointDTO.of(692,607)
                  },
                  new PointDTO[]{
                          PointDTO.of(1,699), PointDTO.of(89,699), PointDTO.of(177, 698),
                          PointDTO.of(263,697), PointDTO.of(350,697), PointDTO.of(437,697),
                          PointDTO.of(523,693), PointDTO.of(607,693), PointDTO.of(692,693),
                  }
          };

          PointDTO[] corners = {
                  PointDTO.of(694, 244), PointDTO.of(706, 928),
                  PointDTO.of(10, 928), PointDTO.of(10, 230)
          };



          return UnresolvedMove.builder()
                               .chessboardImage(chessboardImage)
                               .lastBoard(createInitialBoard())
                               .referenceColors(colors)
                               .tilesCorners(coordsMapper.toTilesCoords(pointDTOS))
                               .chessboardCorners(coordsMapper.toPoints(corners))
                               .build();
     }
}
