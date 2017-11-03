package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.common.PossibleMoveResult;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import lombok.Value;

@Value
public class Move {

   private Coords from;
   private Coords to;
   private Piece piece;
   private PossibleMoveResult moveResult;

   public PlayerColor getPlayer() {
      return piece.getOwner();
   }
}
