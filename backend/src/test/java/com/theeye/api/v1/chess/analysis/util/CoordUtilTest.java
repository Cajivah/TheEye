package com.theeye.api.v1.chess.analysis.util;

import com.sun.javafx.geom.Vec2f;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CoordUtilTest {

     @Nested
     @DisplayName("Given 2d coords")
     class coords2D {

          @Nested
          @DisplayName("when arguments are correct")
          class correct {

               @Nested
               @DisplayName("then return 1D coords")
               class result {

                    @DisplayName("Should return parametrized")
                    @ParameterizedTest(name = "For {0}, {1} should give {2}")
                    @CsvSource({
                            "1, 1, 5, 6",
                            "1, 3, 4, 7",
                            "3, 2, 3, 11",
                    })
                    void toOneDimension(int row, int col, int width, int index) {
                         int result = CoordUtil.toOneDimension(row, col, width);
                         assertEquals(index, result);
                    }
               }
          }
     }

     @Test
     void pointMovedByVector() {
     }

     @Nested
     @DisplayName("Given vector and scale")
     class scale {

          @Nested
          @DisplayName("when arguments are correct")
          class correct {

               @Nested
               @DisplayName("then return scaled vector")
               class result {

                    @DisplayName("Should return:")
                    @ParameterizedTest(name = "For {0}, {1} should give {2}")
                    @CsvSource({
                            "1.0, 1.0, 0.5, 0.5, 0.5",
                            "1.0, 7.0, 1.0, 1.0, 7.0",
                            "3.0, 4.0, 2.0, 6.0, 8.0",
                    })
                    void scaleVector(float x, float y, double scale, float resultX, float resultY) {
                         Vec2f vector = new Vec2f(x, y);
                         Vec2f scaled = CoordUtil.getScaledVectorOf(vector, scale);
                         assertAll("Vector params equal within precision",
                                 () -> assertEquals(resultX, scaled.x, 0.1 ),
                                 () -> assertEquals(resultY, scaled.y, 0.1 ));
                    }
               }
          }
     }

}