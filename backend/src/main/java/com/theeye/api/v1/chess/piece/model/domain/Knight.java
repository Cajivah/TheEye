package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.piece.common.fen.FenCodes;

public class Knight extends Piece{

   private static final String AS_PGN = "N";

   public Knight(PlayerColor playerColor) {
      super(playerColor);
   }

   @Override
   public String toPGN() {
      return AS_PGN;
   }

   @Override
   public char getFenCode() {
      return getOwner().equals(PlayerColor.White)
              ? FenCodes.KNIGHT_WHITE
              : FenCodes.KNIGHT_BLACK;
   }

}
