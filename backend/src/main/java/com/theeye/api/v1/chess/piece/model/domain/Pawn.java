package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.fen.common.FenCodes;

public class Pawn extends Piece {

     private static final String AS_PGN = "";

     public Pawn(PlayerColor playerColor) {
          super(playerColor);
     }

     public static Pawn of(PlayerColor playerColor) {
          return new Pawn(playerColor);
     }

     @Override
     public String toPGN() {
          return AS_PGN;
     }

     @Override
     public char getFenCode() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? FenCodes.PAWN_WHITE
                  : FenCodes.PAWN_BLACK;
     }
}
