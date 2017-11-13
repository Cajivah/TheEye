package com.theeye.api.v1.chess.analysis.util;

import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class LineUtils {

     public List<ParametrizedLine2D> filterHorizontalLines(List<ParametrizedLine2D> parametrizedLines) {
          final Predicate<ParametrizedLine2D> closeToHorizontal = line -> line.a > -1 && line.a <= 1;
          return filterLinesAndSort(parametrizedLines, closeToHorizontal);
     }

     public List<ParametrizedLine2D> filterVerticalLines(List<ParametrizedLine2D> parametrizedLines) {
          final Predicate<ParametrizedLine2D> closeToVertical = line -> line.a <= -1 || line.a > 1;
          return filterLinesAndSort(parametrizedLines, closeToVertical);
     }

     private List<ParametrizedLine2D> filterLinesAndSort(List<ParametrizedLine2D> parametrizedLines,
                                                        Predicate<ParametrizedLine2D> filter) {
          Comparator<ParametrizedLine2D> byCDescending = (line1, line2) -> line1.c < line2.c ? 1 : -1;
          return parametrizedLines.stream()
                                  .filter(filter)
                                  .sorted(byCDescending)
                                  .collect(Collectors.toList());
     }
}
