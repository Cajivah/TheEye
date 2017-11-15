package com.theeye.api.v1.chess.analysis.model.domain;

import com.sun.javafx.geom.Line2D;
import com.theeye.api.v1.chess.analysis.exception.IncorrectBoundsException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.opencv.core.Point;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParametrizedLine2D extends Line2D {

     // Ax+By+C=0
     private double a;
     private double b;
     private double c;

     public ParametrizedLine2D(Line2D line, double a, double b, double c) {
          super(line.x1, line.y1, line.x2, line.y2);
          this.a = a;
          this.b = b;
          this.c = c;
     }

     public Point intersect(ParametrizedLine2D line) {
          double w = this.a * line.b - this.b * line.a;
          if (w == 0) {
               throw new IncorrectBoundsException();
          }
          double wx = this.b * line.c - this.c * line.b;
          double wy = this.c * line.a - this.a * line.c;

          double x = wx/w;
          double y = wy/w;

          return new Point(x, y);
     }
}
