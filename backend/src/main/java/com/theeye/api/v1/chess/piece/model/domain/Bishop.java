package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.fen.common.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

public class Bishop extends Piece {

     private static final String AS_PGN = "B";

     public Bishop(PlayerColor playerColor) {
          super(playerColor);
     }

     public static Bishop of(PlayerColor playerColor) {
          return new Bishop(playerColor);
     }

     @Override
     public String toPGN() {
          return AS_PGN;
     }

     @Override
     public char getFenCode() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? FenCodes.BISHOP_WHITE
                  : FenCodes.BISHOP_BLACK;
     }

     @Override
     public PieceType getPieceType() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? PieceType.BISHOP_WHITE
                  : PieceType.BISHOP_BLACK;
     }
}
