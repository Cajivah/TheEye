package com.theeye.api.v1.chess.piece.common.fen;

import com.theeye.api.factory.BoardTestFactory;
import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import com.theeye.api.v1.chess.fen.parser.FenDecoder;
import com.theeye.api.v1.chess.fen.parser.FenParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.theeye.api.factory.CastlingStatusTestFactory.createAllFalse;
import static com.theeye.api.v1.chess.piece.model.enumeration.PieceType.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FenDecoderTest {

     private FenDecoder sut = new FenDecoder(new FenParser());

     @Nested
     @DisplayName("When converting fen to board")
     public class ParseFenToBoard {


          @Nested
          @DisplayName("Should decode correctly")
          public class DecodeCorrectly {

               @Test
               @DisplayName("Given correct initial position as fen")
               void initialSetUp() {
                    Fen fen = Fen.of(BoardConsts.STARTING_SET_UP_FEN);
                    Board initialBoard = BoardTestFactory.createInitialBoard();

                    Board decoded = sut.decode(fen);

                    IntStream.range(0, BoardConsts.ROWS)
                             .boxed()
                             .forEach(rowIndex ->
                                     assertArrayEquals(
                                             initialBoard.getTiles()[rowIndex],
                                             decoded.getTiles()[rowIndex]));
               }

               @Test
               @DisplayName("Given random correct position as fen")
               void randomPosition() {
                    Fen fen = Fen.of("rn1q3r/p2bk1bp/B1p1P1pn/1p4p1/2PP4/2NQ1P2/PP2N1PP/R4RK1 b - g3 4 8");

                    Board decoded = sut.decode(fen);

                    Tile[][] expected = new Tile[][]{
                            {
                                    Tile.of(ROOK_WHITE.create()), Tile.of(EMPTY.create()), Tile.of(EMPTY.create()),
                                    Tile.of(EMPTY.create()), Tile.of(EMPTY.create()), Tile.of(ROOK_WHITE.create()),
                                    Tile.of(KING_WHITE.create()), Tile.of(EMPTY.create())
                            },
                            {
                                    Tile.of(PAWN_WHITE.create()), Tile.of(PAWN_WHITE.create()), Tile.of(EMPTY.create()),
                                    Tile.of(EMPTY.create()), Tile.of(KNIGHT_WHITE.create()), Tile.of(EMPTY.create()),
                                    Tile.of(PAWN_WHITE.create()), Tile.of(PAWN_WHITE.create())
                            },
                            {
                                    Tile.of(EMPTY.create()), Tile.of(EMPTY.create()), Tile.of(KNIGHT_WHITE.create()),
                                    Tile.of(QUEEN_WHITE.create()), Tile.of(EMPTY.create()), Tile.of(PAWN_WHITE.create()),
                                    Tile.of(EMPTY.create()), Tile.of(EMPTY.create())
                            },
                            {
                                    Tile.of(EMPTY.create()), Tile.of(EMPTY.create()), Tile.of(PAWN_WHITE.create()),
                                    Tile.of(PAWN_WHITE.create()), Tile.of(EMPTY.create()), Tile.of(EMPTY.create()),
                                    Tile.of(EMPTY.create()), Tile.of(EMPTY.create())
                            },
                            {
                                    Tile.of(EMPTY.create()), Tile.of(PAWN_BLACK.create()), Tile.of(EMPTY.create()),
                                    Tile.of(EMPTY.create()), Tile.of(EMPTY.create()), Tile.of(EMPTY.create()),
                                    Tile.of(PAWN_BLACK.create()), Tile.of(EMPTY.create()),
                            },
                            {
                                    Tile.of(BISHOP_WHITE.create()), Tile.of(EMPTY.create()), Tile.of(PAWN_BLACK.create()),
                                    Tile.of(EMPTY.create()), Tile.of(PAWN_WHITE.create()), Tile.of(EMPTY.create()),
                                    Tile.of(PAWN_BLACK.create()), Tile.of(KNIGHT_BLACK.create()),
                            },
                            {
                                    Tile.of(PAWN_BLACK.create()), Tile.of(EMPTY.create()), Tile.of(EMPTY.create()),
                                    Tile.of(BISHOP_BLACK.create()), Tile.of(KING_BLACK.create()), Tile.of(EMPTY.create()),
                                    Tile.of(BISHOP_BLACK.create()), Tile.of(PAWN_BLACK.create()),
                            },
                            {
                                    Tile.of(ROOK_BLACK.create()), Tile.of(KNIGHT_BLACK.create()), Tile.of(EMPTY.create()),
                                    Tile.of(QUEEN_BLACK.create()), Tile.of(EMPTY.create()), Tile.of(EMPTY.create()),
                                    Tile.of(EMPTY.create()), Tile.of(ROOK_BLACK.create()),
                            }
                    };

                    IntStream.range(0, BoardConsts.ROWS)
                             .boxed()
                             .forEach(rowIndex ->
                                     assertArrayEquals(
                                             decoded.getTiles()[rowIndex],
                                             expected[rowIndex])
                             );

                    assertEquals(PlayerColor.BLACK, decoded.getActiveColour());
                    assertEquals(4, decoded.getHalfmoveClock());
                    assertEquals(8, decoded.getFullmoveNumber());
                    assertEquals(createAllFalse(), decoded.getCastling());
                    assertEquals("g3", decoded.getEnPassant());
               }
          }
     }
}