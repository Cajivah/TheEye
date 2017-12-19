package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.fen.model.consts.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

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

     @Override
     public PieceType getPieceType() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? PieceType.PAWN_WHITE
                  : PieceType.PAWN_BLACK;
     }

     @Override
     public Piece copy() {
          return Pawn.of(this.getOwner());
     }
}
