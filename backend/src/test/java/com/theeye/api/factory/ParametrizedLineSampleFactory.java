package com.theeye.api.factory;

import com.sun.javafx.geom.Line2D;
import com.theeye.api.v1.chess.image.analysis.model.domain.ParametrizedLine2D;
import org.assertj.core.util.Lists;

import java.util.List;

public class ParametrizedLineSampleFactory {

     public static List<ParametrizedLine2D> createSome() {
          return Lists.newArrayList(
                  new ParametrizedLine2D(new Line2D(1, 2, 3, 4), 1, 1, 1),
                  new ParametrizedLine2D(new Line2D(1, 2, 2, 4), 2, 1, 0),
                  new ParametrizedLine2D(new Line2D(2, 1, 4, 2), 0.5, 1, 0),
                  new ParametrizedLine2D(new Line2D(3, 1, 5, 2), 0.5, 1, -0.5),
                  new ParametrizedLine2D(new Line2D(0, 4, 10, 0), -0.4, 1, 4)
          );
     }
}
