package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.domain.CastlingStatus;
import com.theeye.api.v1.chess.board.model.domain.PlayersCastlingStatuses;

public class CastlingStatusTestFactory {

     public static PlayersCastlingStatuses createAllTrue() {
          return PlayersCastlingStatuses.builder()
                                        .white(createAllTrueCastlingStatus())
                                        .black(createAllTrueCastlingStatus())
                                        .build();
     }

     private static CastlingStatus createAllTrueCastlingStatus() {
          return CastlingStatus.builder()
                               .kingSideCastle(true)
                               .queenSideCastle(true)
                               .build();
     }

     public static PlayersCastlingStatuses createAllFalse() {
          return PlayersCastlingStatuses.builder()
                                        .white(createAllFalseCastlingStatus())
                                        .black(createAllFalseCastlingStatus())
                                        .build();
     }

     public static CastlingStatus createAllFalseCastlingStatus() {
          return CastlingStatus.builder()
                               .kingSideCastle(false)
                               .queenSideCastle(false)
                               .build();
     }
}
