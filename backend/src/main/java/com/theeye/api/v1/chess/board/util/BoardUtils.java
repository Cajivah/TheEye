package com.theeye.api.v1.chess.board.util;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.Tile;

public class BoardUtils {

     public static Tile[][] shallowCopyTiles(Tile[][] oldTiles) {
          int rows = oldTiles.length;
          Tile[][] newTiles = new Tile[rows][];
          for (int i = 0; i < rows; i++) {
               int cols = oldTiles[i].length;
               Tile[] newTilesRow = new Tile[cols];
               for (int j = 0; j < cols; j++) {
                    newTilesRow[j] = oldTiles[i][j];
               }
               newTiles[i] = newTilesRow;
          }
          return newTiles;
     }

     public static boolean coordsValid(Coords coords) {
          return coords.getRow() < BoardConsts.ROWS
                  && coords.getColumn() < BoardConsts.COLUMNS
                  && coords.getRow() >= 0
                  && coords.getColumn() >= 0;
     }
}
