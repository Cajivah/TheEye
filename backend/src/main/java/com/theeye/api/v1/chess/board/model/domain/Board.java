package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Builder
@Getter
public class Board {

     private Tile tiles[][];
     private PlayerColor activeColour;
     private CastlingStatus castling;
     private String enPassant;
     private int halfmoveClock = 0;
     private int fullmoveNumber = 1;

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
}
