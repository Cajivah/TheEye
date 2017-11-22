package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Board;

import static com.theeye.api.factory.CastlingStatusTestFactory.createAllTrue;

public class BoardTestFactory {

     public static Board createInitialBoard() {
          return Board.builder()
                      .tiles(TileTestFactory.createInitial())
                      .activeColor(PlayerColor.WHITE)
                      .castling(createAllTrue())
                      .enPassant("-")
                      .halfmoveClock(0)
                      .fullmoveNumber(1)
                      .build();
     }

     public static Board createAfter1e4() {
          return Board.builder()
                  .tiles(TileTestFactory.createAfter1e4())
                  .activeColor(PlayerColor.BLACK)
                  .castling(createAllTrue())
                  .enPassant("-")
                  .halfmoveClock(0)
                  .fullmoveNumber(1)
                  .build();
     }

     public static Board createForBeforeTakeSetup1() {
          return Board.builder()
                      .tiles(TileTestFactory.createForBeforeTakeSetup1())
                      .activeColor(PlayerColor.WHITE)
                      .castling(createAllTrue())
                      .enPassant("-")
                      .halfmoveClock(2)
                      .fullmoveNumber(4)
                      .build();
     }


     public static Board createForAfterTakeSetup1() {
          return Board.builder()
                      .tiles(TileTestFactory.createForAfterTakeSetup1())
                      .activeColor(PlayerColor.BLACK)
                      .castling(createAllTrue())
                      .enPassant("-")
                      .halfmoveClock(0)
                      .fullmoveNumber(4)
                      .build();
     }
}
