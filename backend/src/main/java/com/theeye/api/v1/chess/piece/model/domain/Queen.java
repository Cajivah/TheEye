package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.fen.common.FenCodes;

public class Queen extends Piece {

     private static final String AS_PGN = "Q";

     public Queen(PlayerColor playerColor) {
          super(playerColor);
     }

     public static Queen of(PlayerColor playerColor) {
          return new Queen(playerColor);
     }

     @Override
     public String toPGN() {
          return AS_PGN;
     }

     @Override
     public char getFenCode() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? FenCodes.QUEEN_WHITE
                  : FenCodes.QUEEN_BLACK;
     }
}
