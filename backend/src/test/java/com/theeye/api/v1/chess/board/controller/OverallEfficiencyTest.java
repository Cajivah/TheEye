package com.theeye.api.v1.chess.board.controller;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.board.model.dto.ResolvedMoveDTO;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import com.theeye.api.v1.chess.image.analysis.controller.AnalysisController;
import com.theeye.api.v1.chess.image.analysis.model.dto.ChessboardPositionFeaturesDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.PreprocessedChessboardImageDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.ReferenceColorsDTO;
import com.theeye.api.v1.common.util.Base64Util;
import extension.SpringExtension;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import tag.EffectivenessTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@SpringBootTest
@ExtendWith({SpringExtension.class})
@EffectivenessTest
@Slf4j
@Disabled
public class OverallEfficiencyTest {
     public static final String PATH = "./img/";

     @Autowired AnalysisController analysisController;
     @Autowired BoardController boardController;

     private static RestTemplate restTemplate = new RestTemplate();

     @Test
     void kaspBlueReproductionTest() throws IOException {
          String dirName = "kasp-deep/";
          int moveCount = 100;

          runEfficiencyTest(dirName, moveCount);
     }

     @Test
     void allTypes() throws IOException {
          String dirName = "all-types/";
          int moveCount = 65;

          runEfficiencyTest(dirName, moveCount);
     }

     @Test
     void brown() throws IOException {
          String dirName = "brown/";
          int moveCount = 73;

          runEfficiencyTest(dirName, moveCount);
     }

     private void runEfficiencyTest(String dirName, int moveCount) throws IOException {
          ChessboardPositionFeaturesDTO coords = getCoords(dirName);
          ReferenceColorsDTO colors = getColors(dirName, coords);
          ResolvedMoveDTO lastPosition = ResolvedMoveDTO.builder()
                                                        .newPosition(BoardConsts.STARTING_SET_UP_FEN)
                                                        .move("")
                                                        .build();
          Resource resource = new ClassPathResource(PATH + dirName + "fens");
          int i = 1;
          int success = 0;
          try(BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
               for(; i <= moveCount; ++i) {
                    if(i == 39) {
                         System.out.println("hold");
                    }
                    String correctFen = br.readLine();
                    try{
                         ResolvedMoveDTO nextMove = getNextMove(PATH + dirName + i + ".jpg", coords, colors, lastPosition);
                         if(!correctFen.equals(lastPosition.getNewPosition())) {
                              log.warn("DIFFERENCE: " + correctFen);
                         } else {
                              success++;
                         }
                         log.info("[" + i + "]:" + lastPosition + "\n");
                    } catch(Exception ignored) {
                         System.out.println("Failed for i = " + i);
                    }
                    lastPosition = ResolvedMoveDTO.builder()
                                                  .newPosition(correctFen)
                                                  .build();
               }
          } catch (Exception ignored) {

          }
          log.info("Analysed successfully " + success + " out of " + moveCount + " moves");
     }

     private ResolvedMoveDTO getNextMove(String path, ChessboardPositionFeaturesDTO coords, ReferenceColorsDTO colors, ResolvedMoveDTO lastPosition) throws IOException {
          ChessboardImageDTO image = getChessboardImageDTO(path);
          MoveToResolveDTO move = MoveToResolveDTO.builder()
                                                  .chessboardImage(image)
                                                  .lastPosition(Fen.of(lastPosition.getNewPosition()))
                                                  .referenceColors(colors)
                                                  .positions(coords)
                                                  .build();
          return boardController.findNewPosition(move);
     }

     private ReferenceColorsDTO getColors(String dirName, ChessboardPositionFeaturesDTO coords) throws IOException {
          ChessboardImageDTO image = getChessboardImageDTO(PATH + dirName + "colors.jpg");
          PreprocessedChessboardImageDTO colorsInputDTO = PreprocessedChessboardImageDTO.builder()
                                                                                        .chessboardCorners(coords.getChessboardCorners())
                                                                                        .tilesCornerPoints(coords.getTilesCornerPoints())
                                                                                        .image(image)
                                                                                        .build();
          return analysisController.findReferenceColors(colorsInputDTO);
     }

     private ChessboardImageDTO getChessboardImageDTO(String path) throws IOException {
          Resource resource = new ClassPathResource(path);
          byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
          String base64 = Base64Util.getBase64(imageBytes);
          return ChessboardImageDTO.builder()
                                                       .base64Image(base64)
                                                       .build();
     }

     private ChessboardPositionFeaturesDTO getCoords(String dirName) throws IOException {
          ChessboardImageDTO image = getChessboardImageDTO(PATH + dirName + "coords.jpg");
          return analysisController.findChessboardCorners(image);
     }
}
