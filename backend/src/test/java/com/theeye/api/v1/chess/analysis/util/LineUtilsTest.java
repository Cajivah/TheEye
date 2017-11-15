package com.theeye.api.v1.chess.analysis.util;

import com.theeye.api.factory.ParametrizedLineSampleFactory;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineUtilsTest {

     @Nested
     @DisplayName("Given lines to filter")
     class lines {

          List<ParametrizedLine2D> lines;

          @Nested
          @DisplayName("When filtering horizontal")
          class horizontal {

               @BeforeEach
               public void setUp() {
                    lines = ParametrizedLineSampleFactory.createSome();
               }

               @Test
               @DisplayName("Should return")
               void filterHorizontalLines() {
                    List<ParametrizedLine2D> filtered = ParametrizedLineProcessor.of(lines)
                                                                                 .filterHorizontalLines()
                                                                                 .getLines();
                    assertEquals(4, filtered.size());
                    assertEquals(4, filtered.get(0).getC());
               }
          }

          @Nested
          @DisplayName("When filtering vertical")
          class vertical {

               @BeforeEach
               public void setUp() {
                    lines = ParametrizedLineSampleFactory.createSome();
               }

               @Test
               @DisplayName("Should return")
               void filterHorizontalLines() {
                    List<ParametrizedLine2D> filtered = ParametrizedLineProcessor.of(lines)
                                                                                 .filterVerticalLines()
                                                                                 .getLines();
                    assertEquals(1, filtered.size());
                    assertEquals(0, filtered.get(0).getC());
               }
          }
     }
}