package com.theeye.api.v1.chess.piece.common.fen;

import com.theeye.api.factory.BoardTestFactory;
import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import com.theeye.api.v1.chess.fen.parser.FenEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FenEncoderTest {

     FenEncoder sut = new FenEncoder();

     @Nested
     @DisplayName("Given correct board")
     class GivenBoard {

          @Nested
          @DisplayName("When encoding")
          class Encode {

               @Test
               @DisplayName("Should give correct initial fen")
               void encodeInitial() {
                    Board initialBoard = BoardTestFactory.createInitialBoard();
                    Fen encoded = sut.encode(initialBoard);
                    assertEquals(BoardConsts.STARTING_SET_UP_FEN, encoded.getFenDescription());
               }

          }

     }

}