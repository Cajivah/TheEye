package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.board.model.enumeration.PossibleMoveResult;
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
