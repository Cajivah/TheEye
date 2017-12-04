package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;

public class ChessboardImageDTOTestFactory {

     public static ChessboardImageDTO createFromBase64(String image) {
          return ChessboardImageDTO.builder()
                                   .base64Image(image)
                                   .build();
     }
}
