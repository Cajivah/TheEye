package com.theeye.api.v1.chess.board.service;

import com.theeye.api.factory.BoardTestFactory;
import com.theeye.api.factory.ChangesTestFactory;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.moveresolver.BoardDetailsUpdater;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

     BoardDetailsUpdater boardDetailsUpdater = new BoardDetailsUpdater();

     BoardService sut = new BoardService(boardDetailsUpdater);

     @Nested
     @DisplayName("Given move to commit")
     class CommitMove {

          Board lastBoard;
          List<TileChange> changes;

          @Nested
          @DisplayName("When type is regular")
          class Regular {

               @Test
               @DisplayName("Should commit and update fen")
               void doMove() {
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
          }
     }
}