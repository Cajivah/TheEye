package com.theeye.api.v1.chess.image.analysis.mapper;

import com.sun.javafx.geom.Line2D;
import com.theeye.api.v1.chess.image.analysis.model.domain.ParametrizedLine2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineMapperTest {

     private LineMapper sut = new LineMapper();

     @Nested
     @DisplayName("Given line as points")
     class line{

          @Nested
          @DisplayName("When input is correct")
          class parametrize {

               @DisplayName("Should return parametrized")
               @ParameterizedTest(name = "For {0}, {1}, {2}, {3} should give a={4}, b={5}, c={6}")
               @CsvSource({
                       "1, 1, 1, 2, 1, 0, -1",
                       "1, 1, 2, 1, 0, 1, -1",
                       "1, 2, 1, 2, 0, 0, 0",
                       "1, 2, 3, 4, -1, 1, -1",
                       "1, 2, 2, 4, -2, 1, 0",
               })
               void toParametrizedLine(float x1, float y1, float x2, float y2, double a, double b, double c) {
                    Line2D line = new Line2D(x1, y1, x2, y2);
                    ParametrizedLine2D parametrizedLine2D = sut.toParametrizedLine(line);
                    assertAll("Correct parameters",
                            () -> assertEquals(a, parametrizedLine2D.getA(), 0.1),
                            () -> assertEquals(b, parametrizedLine2D.getB(), 0.1),
                            () -> assertEquals(c, parametrizedLine2D.getC(), 0.1));

               }
          }
     }

}