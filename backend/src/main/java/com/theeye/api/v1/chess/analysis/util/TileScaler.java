package com.theeye.api.v1.chess.analysis.util;

import com.sun.javafx.geom.Vec2f;
import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

import static com.theeye.api.v1.chess.analysis.util.CoordUtil.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TileScaler {

     public static TileCorners computeScaledTile(TileCorners tile, double sideScale) {
          Vec2f topLeftBotRightDiagonalScalingVec =
                  computeScalingVector(tile.getTopLeft(), tile.getBottomRight(), sideScale);
          Vec2f topRightBotLeftDiagonalScalingVec =
                  computeScalingVector(tile.getTopRight(), tile.getBottomLeft(), sideScale);

          Vec2f topLeftScalingVec = getScaledVectorOf(topLeftBotRightDiagonalScalingVec, 0.5);
          Vec2f topRightScalingVec = getScaledVectorOf(topRightBotLeftDiagonalScalingVec, 0.5);
          Vec2f botLeftScalingVec = getScaledVectorOf(topRightBotLeftDiagonalScalingVec, -0.5);
          Vec2f botRightScalingVec = getScaledVectorOf(topLeftBotRightDiagonalScalingVec, -0.5);

          return TileCorners.builder()
                            .topLeft(pointMovedByVector(tile.getTopLeft(), topLeftScalingVec))
                            .topRight(pointMovedByVector(tile.getTopRight(), topRightScalingVec))
                            .bottomRight(pointMovedByVector(tile.getBottomRight(), botRightScalingVec))
                            .bottomLeft(pointMovedByVector(tile.getBottomLeft(), botLeftScalingVec))
                            .build();
     }

     private static Vec2f computeScalingVector(Point from, Point to, double sideScale) {
          Vec2f vector = toVector(from, to);
          return getScaledVectorOf(vector, sideScale);
     }
}
