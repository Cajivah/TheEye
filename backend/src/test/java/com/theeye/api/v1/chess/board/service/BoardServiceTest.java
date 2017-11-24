package com.theeye.api.v1.chess.board.service;

import com.theeye.api.factory.BoardTestFactory;
import com.theeye.api.factory.ChangesTestFactory;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

     BoardDetailsUpdaterService boardDetailsUpdaterService = new BoardDetailsUpdaterService();

     BoardService sut = new BoardService(boardDetailsUpdaterService);

     @Nested
     @DisplayName("Given move to commit")
     class CommitMove {

          Board lastBoard;
          List<TileChange> changes;

          @Nested
          @DisplayName("When type is regular")
          class Regular {

               @Test
               @DisplayName("Should commit regular move and update fen")
               void doRegularMove() {
                    lastBoard = BoardTestFactory.createInitialBoard();
                    changes = ChangesTestFactory.createChangesAfter1e4();

                    Board board = sut.doMove(lastBoard, changes, MoveType.REGULAR);

                    Board expected = BoardTestFactory.createAfter1e4();
                    assertAll(
                            () -> assertArrayEquals(expected.getTiles(), board.getTiles()),
                            () -> assertEquals(expected.getActiveColor(), board.getActiveColor()),
                            () -> assertEquals(expected.getEnPassant(), board.getEnPassant()),
                            () -> assertEquals(expected.getFullmoveNumber(), board.getFullmoveNumber()),
                            () -> assertEquals(expected.getHalfmoveClock(), board.getHalfmoveClock()),
                            () -> assertEquals(expected.getCastling(), board.getCastling())
                    );
               }

               @Test
               @DisplayName("Should commit taking move and update fen")
               void doTakingMove() {
                    lastBoard = BoardTestFactory.createForBeforeTakeSetup1();
                    changes = ChangesTestFactory.createChangesAfterTakeSetup1();

                    Board board = sut.doMove(lastBoard, changes, MoveType.REGULAR);

                    Board expected = BoardTestFactory.createForAfterTakeSetup1();
                    assertAll(
                            () -> assertArrayEquals(expected.getTiles(), board.getTiles()),
                            () -> assertEquals(expected.getActiveColor(), board.getActiveColor()),
                            () -> assertEquals(expected.getEnPassant(), board.getEnPassant()),
                            () -> assertEquals(expected.getFullmoveNumber(), board.getFullmoveNumber()),
                            () -> assertEquals(expected.getHalfmoveClock(), board.getHalfmoveClock()),
                            () -> assertEquals(expected.getCastling(), board.getCastling())
                    );
               }

               @Test
               @DisplayName("Should commit en passant move and update fen")
               void doEnPassantMove() {
                    lastBoard = BoardTestFactory.createForAfterEnPassantPossibleSetup1();
                    changes = ChangesTestFactory.createChangesAfterEnPassantSetup1();

                    Board board = sut.doMove(lastBoard, changes, MoveType.EN_PASSANT);

                    Board expected = BoardTestFactory.createForAfterEnPassantSetup1();
                    assertAll(
                            () -> assertArrayEquals(expected.getTiles(), board.getTiles()),
                            () -> assertEquals(expected.getActiveColor(), board.getActiveColor()),
                            () -> assertEquals(expected.getEnPassant(), board.getEnPassant()),
                            () -> assertEquals(expected.getFullmoveNumber(), board.getFullmoveNumber()),
                            () -> assertEquals(expected.getHalfmoveClock(), board.getHalfmoveClock()),
                            () -> assertEquals(expected.getCastling(), board.getCastling())
                    );
               }

               @Test
               @DisplayName("Should commit en passant move and update fen with en passant coords")
               void doPreEnPassantMove() {
                    lastBoard = BoardTestFactory.createForBeforeEnPassantPossibleSetup1();
                    changes = ChangesTestFactory.createChangesAfterEnPassantPossibleSetup1();

                    Board board = sut.doMove(lastBoard, changes, MoveType.REGULAR);

                    Board expected = BoardTestFactory.createForAfterEnPassantPossibleSetup1();
                    assertAll(
                            () -> assertArrayEquals(expected.getTiles(), board.getTiles()),
                            () -> assertEquals(expected.getActiveColor(), board.getActiveColor()),
                            () -> assertEquals(expected.getEnPassant(), board.getEnPassant()),
                            () -> assertEquals(expected.getFullmoveNumber(), board.getFullmoveNumber()),
                            () -> assertEquals(expected.getHalfmoveClock(), board.getHalfmoveClock()),
                            () -> assertEquals(expected.getCastling(), board.getCastling())
                    );
               }
          }
     }
}