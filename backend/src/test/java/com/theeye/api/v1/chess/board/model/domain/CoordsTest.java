package com.theeye.api.v1.chess.board.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoordsTest {

     @Nested
     @DisplayName("Given coords to converto to string")
     class ToString {

          @Nested
          @DisplayName("Coords correct")
          class CorrectCoords {

               @Nested
               @DisplayName("Should return chessboard coords")
               class InvertAndToString{

                    @DisplayName("Should return ")
                    @ParameterizedTest(name = "For {0}, {1} should give {2}")
                    @CsvSource({
                            "0, 0, a1",
                            "1, 3, b4",
                            "3, 1, d2",
                            "7, 7, h8"
                    })
                    void toInvertedChessboardString(int row, int col, String result) {
                         Coords coords = Coords.builder()
                                               .row(row)
                                               .column(col)
                                               .build();
                         assertEquals(result, coords.toInvertedChessboardString());
                    }
               }
          }
     }
}