package com.theeye.api.v1.chess.fen.parser;

import com.theeye.api.factory.BoardTestFactory;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FenEncoderTest {

     FenEncoder sut = new FenEncoder();

     @Nested
     @DisplayName("Converting board to fen.")
     class FenToBoard {

          @Nested
          @DisplayName("With correct board.")
          class Encode {

               @Test
               @DisplayName("Should give correct initial fen.")
               void encodeInitial() {
                    Board initialBoard = BoardTestFactory.createInitialBoard();
                    Fen encoded = sut.encode(initialBoard);
                    assertEquals(BoardConsts.STARTING_SET_UP_FEN, encoded.getFenDescription());
               }
          }
     }
}