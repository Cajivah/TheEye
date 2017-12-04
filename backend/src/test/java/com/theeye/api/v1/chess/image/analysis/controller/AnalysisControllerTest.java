package com.theeye.api.v1.chess.image.analysis.controller;

import com.theeye.api.factory.ChessboardImageDTOTestFactory;
import com.theeye.api.factory.ResourcesWrapper;
import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import extension.SpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tag.EffectivenessTest;

import java.util.List;
import java.util.stream.Collectors;

import static com.theeye.api.factory.ResourcesWrapper.IMAGES;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith({SpringExtension.class})
@EffectivenessTest
class AnalysisControllerTest {

     @Autowired
     private AnalysisController sut;

     @Test
     void findChessboardCorners() {
          List<ChessboardImageDTO> inputImages =
                  IMAGES.stream()
                        .map(ResourcesWrapper::getResourceAsBase64)
                        .map(ChessboardImageDTOTestFactory::createFromBase64)
                        .collect(Collectors.toList());
          for (ChessboardImageDTO image : inputImages) {
               assertNotNull(sut.findChessboardCorners(image));
          }
     }
}