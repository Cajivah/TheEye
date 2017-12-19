package com.theeye.api.v1.chess.piece.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;
import lombok.Data;

@Data
public abstract class Piece {

     private PlayerColor owner;

     public Piece(PlayerColor playerColor) {
          this.owner = playerColor;
     }

     public abstract String toPGN();

     public abstract char getFenCode();

     public abstract PieceType getPieceType();

     public abstract Piece copy();
}
