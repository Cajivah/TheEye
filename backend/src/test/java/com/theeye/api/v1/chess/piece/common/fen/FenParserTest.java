package com.theeye.api.v1.chess.piece.common.fen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenParserTest {

     private final static String STARTING_SET_UP_AS_FEN =
             "rnbqkbnr/pppppppp/11111111/11111111/11111111/11111111/PPPPPPPP/RNBQKBNR";

     private FenParser sut = new FenParser();

     @Nested
     @DisplayName("When converting fen to board")
     public class parseFenToBoard {

          @Test
          void decode() {
          }

     }

     @Nested
     @DisplayName("When converting board to fen")
     public class parseBoardToFen {

          @Test
          void encode() {
          }

     }

}