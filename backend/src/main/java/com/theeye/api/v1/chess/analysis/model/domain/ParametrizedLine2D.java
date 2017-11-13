package com.theeye.api.v1.chess.analysis.model.domain;

import com.sun.javafx.geom.Line2D;

public class ParametrizedLine2D extends Line2D {

     // Ax+By+C=0
     public double a;
     public double b;
     public double c;

     public ParametrizedLine2D(Line2D line, double a, double b, double c) {
          super(line.x1, line.y1, line.x2, line.y2);
          this.a = a;
          this.b = b;
          this.c = c;
     }
}
