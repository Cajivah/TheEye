package com.theeye.api.v1.chess.image.analysis.util;

import com.sun.javafx.geom.Vec2f;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TileScaler {

     public static TileCorners computeScaledTile(TileCorners tile, double sideScale) {
          Vec2f topLeftBotRightDiagonalScalingVec =
                  computeScalingVector(tile.getTopLeft(), tile.getBottomRight(), sideScale);
          Vec2f topRightBotLeftDiagonalScalingVec =
                  computeScalingVector(tile.getTopRight(), tile.getBottomLeft(), sideScale);

          Vec2f topLeftScalingVec = CoordUtil.getScaledVectorOf(topLeftBotRightDiagonalScalingVec, 0.5);
          Vec2f topRightScalingVec = CoordUtil.getScaledVectorOf(topRightBotLeftDiagonalScalingVec, 0.5);
          Vec2f botLeftScalingVec = CoordUtil.getScaledVectorOf(topRightBotLeftDiagonalScalingVec, -0.5);
          Vec2f botRightScalingVec = CoordUtil.getScaledVectorOf(topLeftBotRightDiagonalScalingVec, -0.5);

          return TileCorners.builder()
                            .topLeft(CoordUtil.pointMovedByVector(tile.getTopLeft(), topLeftScalingVec))
                            .topRight(CoordUtil.pointMovedByVector(tile.getTopRight(), topRightScalingVec))
                            .bottomRight(CoordUtil.pointMovedByVector(tile.getBottomRight(), botRightScalingVec))
                            .bottomLeft(CoordUtil.pointMovedByVector(tile.getBottomLeft(), botLeftScalingVec))
                            .build();
     }

     private static Vec2f computeScalingVector(Point from, Point to, double sideScale) {
          Vec2f vector = CoordUtil.toVector(from, to);
          return CoordUtil.getScaledVectorOf(vector, sideScale);
     }
}
