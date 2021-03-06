package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.fen.model.consts.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

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

     @Override
     public PieceType getPieceType() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? PieceType.ROOK_WHITE
                  : PieceType.ROOK_BLACK;
     }

     @Override
     public Piece copy() {
          return Rook.of(this.getOwner());
     }
}
