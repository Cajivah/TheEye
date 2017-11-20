package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.piece.model.domain.*;
import org.junit.Test;

import java.util.stream.IntStream;

import static com.theeye.api.v1.chess.board.common.PlayerColor.BLACK;
import static com.theeye.api.v1.chess.board.common.PlayerColor.NONE;
import static com.theeye.api.v1.chess.board.common.PlayerColor.WHITE;

public class TileTestFactory {

     public static Tile[][] createInitial() {
          return new Tile[][]{
                  createFirstRow(WHITE),
                  createPawnRow(WHITE),
                  createEmptyRow(),
                  createEmptyRow(),
                  createEmptyRow(),
                  createEmptyRow(),
                  createPawnRow(BLACK),
                  createFirstRow(BLACK)
          };
     }

     private static Tile[] createFirstRow(PlayerColor playerColor) {
          return new Tile[]
                  {
                          Tile.of(Rook.of(playerColor)),
                          Tile.of(Knight.of(playerColor)),
                          Tile.of(Bishop.of(playerColor)),
                          Tile.of(Queen.of(playerColor)),
                          Tile.of(King.of(playerColor)),
                          Tile.of(Bishop.of(playerColor)),
                          Tile.of(Knight.of(playerColor)),
                          Tile.of(Rook.of(playerColor))
                  };
     }

     public static Tile[] createPawnRow(PlayerColor playerColor) {
          return IntStream.range(0, BoardConsts.COLUMNS)
                          .boxed()
                          .map(i -> Tile.of(Pawn.of(playerColor)))
                          .toArray(Tile[]::new);
     }

     public static Tile[] createEmptyRow() {
          return IntStream.range(0, BoardConsts.COLUMNS)
                          .boxed()
                          .map(i -> Tile.of(Empty.of(NONE)))
                          .toArray(Tile[]::new);
     }

     public static Tile[][] createAfter1e4() {
          return new Tile[][]{
                  createFirstRow(WHITE),
                  new Tile[]
                          {
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Pawn.of(WHITE))
                          },
                  createEmptyRow(),
                  new Tile[]
                          {
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Pawn.of(WHITE)),
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Empty.of(NONE)),
                                  Tile.of(Empty.of(NONE))
                          },
                  createEmptyRow(),
                  createEmptyRow(),
                  createPawnRow(BLACK),
                  createFirstRow(BLACK)
          };
     }
}
