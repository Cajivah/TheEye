package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.fen.model.consts.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

public class King extends Piece {

     private static final String AS_PGN = "K";

     public King(PlayerColor playerColor) {
          super(playerColor);
     }

     public static King of(PlayerColor playerColor) {
          return new King(playerColor);
     }

     @Override
     public String toPGN() {
          return AS_PGN;
     }

     @Override
     public char getFenCode() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? FenCodes.KING_WHITE
                  : FenCodes.KING_BLACK;
     }

     @Override
     public PieceType getPieceType() {
          return getOwner().equals(PlayerColor.WHITE)
                  ? PieceType.KING_WHITE
                  : PieceType.KING_BLACK;
     }
}
