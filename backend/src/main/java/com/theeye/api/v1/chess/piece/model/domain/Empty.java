package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

public class Empty extends Piece {

     public Empty(PlayerColor playerColor) {
          super(playerColor);
     }

     public static Empty of(PlayerColor playerColor) {
          return new Empty(playerColor);
     }

     @Override
     public String toPGN() {
          return null;
     }

     @Override
     public char getFenCode() {
          return '1';
     }

     @Override
     public PieceType getPieceType() {
          return PieceType.EMPTY;
     }
}
