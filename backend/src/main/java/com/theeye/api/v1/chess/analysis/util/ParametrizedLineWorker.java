package com.theeye.api.v1.chess.analysis.util;

import com.google.common.collect.Lists;
import com.sun.javafx.geom.Point2D;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@AllArgsConstructor(staticName = "of")
public class ParametrizedLineWorker {

     @Getter
     private List<ParametrizedLine2D> lines;

     public static final int FURTHER_BOUND = 0;
     public static final int CLOSER_BOUND = 1;

     private final static Predicate<ParametrizedLine2D> closeToHorizontal = line -> line.b != 0 && line.a > -1 && line.a <= 1;
     private final static Predicate<ParametrizedLine2D> closeToVertical = line -> line.a <= -1 || line.a > 1 || line.b == 0;


     public ParametrizedLineWorker filterHorizontalLines() {
          lines = filterLines(lines, closeToHorizontal);
          return this;
     }

     public ParametrizedLineWorker filterVerticalLines() {
          lines = filterLines(lines, closeToVertical);
          return this;
     }

     private List<ParametrizedLine2D> filterLines(List<ParametrizedLine2D> parametrizedLines,
                                                  Predicate<ParametrizedLine2D> filter) {
          return parametrizedLines.stream()
                                  .filter(filter)
                                  .collect(Collectors.toList());
     }

     public ParametrizedLineWorker sortByDistance(Point2D distanceFrom) {
          final Comparator<ParametrizedLine2D> byDistanceDescending = (line1, line2) -> line1.ptLineDist(distanceFrom) < line2.ptLineDist(distanceFrom) ? 1 : -1;
          lines = lines.stream()
                       .sorted(byDistanceDescending)
                       .collect(Collectors.toList());
          return this;
     }

     public ParametrizedLineWorker firstAndLast() {
          lines = Lists.newArrayList(lines.get(0), lines.get(lines.size() - 1));
          return this;
     }
}
