package com.theeye.api.v1.chess.analysis.util;

import com.sun.javafx.geom.Vec2f;
import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import static com.theeye.common.ComparisionUtil.getBigger;
import static com.theeye.common.ComparisionUtil.getSmaller;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoordUtil {

     public static int toOneDimension(int row, int col, int rowWidth) {
          return row * rowWidth + col;
     }

     public static Vec2f toVector(Point start, Point end) {
          return new Vec2f((float) (end.x - start.x), (float) (end.y - start.y));
     }

     public static Point pointMovedByVector(Point point, Vec2f vector) {
          return new Point(point.x + vector.x, point.y + vector.y);
     }

     public static Vec2f getScaledVectorOf(Vec2f vector, double scale) {
          double newVectorX = vector.x * scale;
          double newVectorY = vector.y * scale;
          return new Vec2f((float) newVectorX, (float) newVectorY);
     }

     public static Rect innerRect(TileCorners corners) {
          Point leftTopPoint =
                  new Point(
                          getBigger(corners.getBottomLeft().x, corners.getTopLeft().x),
                          getBigger(corners.getTopLeft().y, corners.getTopRight().y)
                  );

          Point rightBottomPoint =
                  new Point(
                          getSmaller(corners.getBottomRight().x, corners.getTopRight().x),
                          getSmaller(corners.getBottomLeft().y, corners.getBottomRight().y)
                  );
          return new Rect(leftTopPoint, rightBottomPoint);
     }


     public static double findDistance3D(Scalar tileColor, Scalar reference) {
          int dimensionsCount = 3;
          return findDistance(tileColor, reference, dimensionsCount);
     }

     public static double findDistance(Scalar tileColor, Scalar reference, int dimensionsCount) {
          double[] tileColors = tileColor.val;
          double[] referenceColors = reference.val;
          double distance = 0;
          for (int i = 0; i < dimensionsCount; ++i) {
               double colorA = tileColors[i];
               double colorB = referenceColors[i];
               distance += Math.pow(colorA - colorB, 2);
          }
          return Math.pow(distance, 0.5);
     }
}
