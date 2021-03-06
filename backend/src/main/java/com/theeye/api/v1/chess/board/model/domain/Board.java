package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.board.util.BoardUtils;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(staticName = "of")
@Builder
@Getter
@Setter
public class Board {

     private Tile[][] tiles;
     private PlayerColor activeColor;
     private PlayersCastlingStatuses castling;
     private String enPassant;
     @Builder.Default
     private int halfmoveClock = 0;
     @Builder.Default
     private int fullmoveNumber = 0;
     private String lastMove; //long algebraic notation


     public Tile getTileAt(int row, int col) {
          return tiles[row][col];
     }

     public Tile getTileAt(Coords coords) {
          return getTileAt(coords.getRow(), coords.getColumn());
     }

     public void unoccupyTileAt(int row, int col) {
          getTileAt(row, col).unoccupy();
     }

     public void setTileAt(Coords coords, Tile tile) {
          tiles[coords.getRow()][coords.getRow()] = tile;
     }

     public boolean hasPieceOnCoords(Coords coords, PieceType pieceType) {
          if(BoardUtils.coordsValid(coords)) {
               return getTileAt(coords).getPiece().getPieceType().equals(pieceType);
          }
          return false;
     }
}


