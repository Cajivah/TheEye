package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.fen.common.FenCodes;

public class Rook extends Piece {

     private static final String AS_PGN = "R";

     public Rook(PlayerColor playerColor) {
          super(playerColor);
     }

     public static Rook of(PlayerColor playerColor) {
          return new Rook(playerColor);
     }

     @Override
     public String toPGN() {
          return AS_PGN;
     }

     @Override
     public char getFenCode() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? FenCodes.ROOK_WHITE
                  : FenCodes.ROOK_BLACK;
     }
}
