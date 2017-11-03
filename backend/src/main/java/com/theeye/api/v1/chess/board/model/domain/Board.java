package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.model.dto.MoveDTO;
import com.theeye.api.v1.chess.board.model.dto.MoveResultDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Board {

   private Tile tiles[][];

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
