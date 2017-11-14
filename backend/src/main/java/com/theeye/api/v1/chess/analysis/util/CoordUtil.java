package com.theeye.api.v1.chess.analysis.util;

import com.sun.javafx.geom.Vec2f;
import org.opencv.core.Point;

public class CoordUtil {

     public static int toOneDimension(int row, int col, int rowWidth) {
          return row * rowWidth + col;
     }

     public static Vec2f toVector(Point start, Point end) {
          return new Vec2f((float)(end.x-start.x), (float)(end.y-start.y));
     }

     public static Point pointMovedByVector(Point point, Vec2f vector) {
          return new Point(point.x + vector.x, point.y + vector.y);
     }
}
