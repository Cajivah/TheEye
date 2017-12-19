package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.fen.model.consts.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

public class Knight extends Piece {

     private static final String AS_PGN = "N";

     public Knight(PlayerColor playerColor) {
          super(playerColor);
     }

     public static Knight of(PlayerColor playerColor) {
          return new Knight(playerColor);
     }

     @Override
     public String toPGN() {
          return AS_PGN;
     }

     @Override
     public char getFenCode() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? FenCodes.KNIGHT_WHITE
                  : FenCodes.KNIGHT_BLACK;
     }

     @Override
     public PieceType getPieceType() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? PieceType.KNIGHT_WHITE
                  : PieceType.KNIGHT_BLACK;
     }

     @Override
     public Piece copy() {
          return Knight.of(this.getOwner());
     }
}
