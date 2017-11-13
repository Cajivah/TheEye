package com.theeye.api.v1.chess.analysis.mapper;

import com.sun.javafx.geom.Line2D;
import com.theeye.api.v1.chess.analysis.model.domain.ParametrizedLine2D;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class LineMapper {

     public ParametrizedLine2D toParametrizedLine(Line2D line) {
          double a, b, c;

          if(line.x1 == line.x2 && line.y1 == line.y2) {
               a = 0;
               b = 0;
               c = 0;
          } else if(line.y1== line.y2) {
               a = 0;
               b = 1;
               c = -line.y1;
          } else if (line.x1 == line.x2) {
               a = 1;
               b = 0;
               c = -line.x1;
          } else {
               a = (line.y1 - line.y2)/(line.x1-line.x2);
               b = 1;
               c = line.y1 - a * line.x1;
          }
          return new ParametrizedLine2D(line, a, b, c);
     }

     public ParametrizedLine2D toParametrizedLine(double[] points) {
          return toParametrizedLine(new Line2D((float)points[0], (float)points[1], (float)points[2], (float)points[3]));
     }
}
