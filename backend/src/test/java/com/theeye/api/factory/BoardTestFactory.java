package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.piece.model.domain.*;

import java.util.stream.IntStream;

import static com.theeye.api.factory.CastlingStatusTestFactory.createAllTrue;
import static com.theeye.api.v1.chess.board.common.PlayerColor.*;

public class BoardTestFactory {

     public static Board createInitialBoard() {
          Tile[][] tiles = new Tile[][]{
                  createFirstRow(White),
                  createPawnRow(White),
                  createEmptyRow(),
                  createEmptyRow(),
                  createEmptyRow(),
                  createEmptyRow(),
                  createPawnRow(Black),
                  createFirstRow(Black)
          };


          return Board.builder()
                      .tiles(tiles)
                      .activeColour(PlayerColor.White)
                      .castling(createAllTrue())
                      .enPassant("-")
                      .halfmoveClock(0)
                      .fullmoveNumber(1)
                      .build();
     }

     private static Tile[] createFirstRow(PlayerColor playerColor) {
          return new Tile[]
                  {
                          Tile.of(Rook.of(playerColor)), Tile.of(Knight.of(playerColor)),
                          Tile.of(Bishop.of(playerColor)), Tile.of(Queen.of(playerColor)),
                          Tile.of(King.of(playerColor)), Tile.of(Bishop.of(playerColor)),
                          Tile.of(Knight.of(playerColor)), Tile.of(Rook.of(playerColor))
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
                          .map(i -> Tile.of(Empty.of(None)))
                          .toArray(Tile[]::new);
     }
}
