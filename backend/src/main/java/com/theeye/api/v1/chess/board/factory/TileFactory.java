package com.theeye.api.v1.chess.board.factory;


import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.piece.model.domain.Piece;

import java.util.Optional;
import java.util.stream.IntStream;

public class TileFactory {

   public static Tile[][] createInitialTiles() {
      Tile[][] tiles = new Tile[BoardConsts.ROWS][BoardConsts.COLUMNS];
      IntStream.range(0, BoardConsts.ROWS)
               .boxed()
               .forEach(row -> IntStream.range(0, BoardConsts.COLUMNS)
                                        .boxed()
                                        .forEach(column -> tiles[row][column] = createEmptyTile()));
      return tiles;
   }

   public static Tile createEmptyTile() {
      return Tile.builder()
                 .piece(Optional.empty())
                 .build();
   }

   public static Tile createWithPiece(Piece piece) {
      return Tile.builder()
                 .piece(Optional.of(piece))
                 .build();
   }
}
