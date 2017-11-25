package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import com.theeye.api.v1.chess.fen.parser.FenDecoder;
import com.theeye.api.v1.chess.fen.parser.FenParser;
import com.theeye.api.v1.chess.piece.model.domain.*;

import java.util.stream.IntStream;

import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.*;

public class TileTestFactory {

     private static FenParser fenParser = new FenParser();
     private static FenDecoder fenDecoder = new FenDecoder(fenParser);

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

     public static Tile[][] createForAfterTakeSetup1() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenAfterTakeSetup1)).getTiles();
     }
     public static Tile[][] createForBeforeTakeSetup1() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenBeforeTakeSetup1)).getTiles();
     }

     public static Tile[][] createForAfterEnPassantPossibleSetup1() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenAfterEnPassantPossibleSetup1)).getTiles();
     }

     public static Tile[][] createForBeforeEnPassantPossibleSetup1() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenBeforeEnPassantPossibleSetup1)).getTiles();
     }

     public static Tile[][] createForAfterEnPassantSetup1() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenAfterEnPassantSetup1)).getTiles();
     }

     public static Tile[][] createBeforeKingSideCastle() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenBeforeKingSideCastling)).getTiles();
     }

     public static Tile[][] createAfterKingSideCastle() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenAfterKingSideCastling)).getTiles();
     }

     public static Tile[][] createBeforeQueenSideCastle() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenBeforeQueenSideCastling)).getTiles();
     }

     public static Tile[][] createAfterQueenSideCastle() {
          return fenDecoder.decode(Fen.of(FenTestFactory.fenAfterQueenSideCastling)).getTiles();
     }
}
