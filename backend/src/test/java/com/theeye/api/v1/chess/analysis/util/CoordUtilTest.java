package com.theeye.api.v1.chess.analysis.util;

import com.sun.javafx.geom.Vec2f;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opencv.core.Scalar;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

     @Nested
     @DisplayName("Given two points in 3d space")
     class distance {

          @Nested
          @DisplayName("when arguments are correct")
          class correct {

               @Nested
               @DisplayName("then return distance")
               class result {

                    @DisplayName("Should return ")
                    @ParameterizedTest(name = "For {0}, {1} should give {2} with 0.1 precision")
                    @CsvSource({
                            "1, 1, 5, 1, 1, 6, 1",
                            "1, 3, 4, 4, 3, 1, 4.24",
                            "1, 1, 1, 1, 1, 1, 0",
                    })
                    void findDistance3D(double x1, double y1, double z1, double x2, double y2, double z2, double result) {
                         Scalar scalarA = new Scalar(x1, y1, z1);
                         Scalar scalarB = new Scalar(x2, y2, z2);
                         double distance3D = CoordUtil.findDistance3D(scalarA, scalarB);
                         assertEquals(result, distance3D, 0.1);
                    }
               }
          }
     }
}