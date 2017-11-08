package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.domain.CastlingStatus;

public class CastlingStatusTestFactory {

     public static CastlingStatus createAllTrue() {
          return CastlingStatus.builder()
                               .kingSideWhiteValid(true)
                               .queenSideWhiteValid(true)
                               .kingSideBlackValid(true)
                               .queenSideBlackValid(true)
                               .build();
     }

     public static CastlingStatus createAllFalse() {
          return CastlingStatus.builder()
                               .kingSideWhiteValid(false)
                               .queenSideWhiteValid(false)
                               .kingSideBlackValid(false)
                               .queenSideBlackValid(false)
                               .build();
     }
}
