package com.theeye.api.v1.chess.analysis.service.position;

import com.sun.javafx.geom.Vec2f;
import com.theeye.api.v1.chess.analysis.util.CoordUtil;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_HEIGHT;
import static com.theeye.api.v1.chess.analysis.service.AnalysisService.CORNERS_PATTERN_WIDTH;
import static com.theeye.api.v1.chess.analysis.util.CoordUtil.toOneDimension;
import static com.theeye.api.v1.chess.analysis.util.CoordUtil.toVector;

@Service
public class TileCornersService {

     private static final int HORIZONTAL_INTERSECTIONS = 9;
     private static final int VERTICAL_INTERSECTIONS = 9;


     public Point[][] findMissingBorderCorners(MatOfPoint2f matOfInnerCorners) {
          Point[][] corners = new Point[HORIZONTAL_INTERSECTIONS][VERTICAL_INTERSECTIONS];
          fillInnerCorners(matOfInnerCorners, corners);
          fillMissing(corners);
          return corners;
     }

     private void fillMissing(Point[][] corners) {
          fillTopRow(corners);
          fillBottomRow(corners);
          fillRightColumn(corners);
          fillLeftColumn(corners);
     }

     private void fillTopRow(Point[][] corners) {
          fillMissingRowExcludingCorners(corners, 2, 1, 0);
     }

     private void fillBottomRow(Point[][] corners) {
          fillMissingRowExcludingCorners(corners, 6, 7, 8);
     }

     private void fillMissingRowExcludingCorners(Point[][] corners, int previousRow, int focusedRow, int missingRow) {
          for(int i = 1; i < HORIZONTAL_INTERSECTIONS - 1; ++i) {
               Point focusedPoint = corners[focusedRow][i];
               Point nextPointDown = corners[previousRow][i];
               Vec2f vector = toVector(nextPointDown, focusedPoint);
               corners[missingRow][i] = CoordUtil.pointMovedByVector(focusedPoint, vector);
          }
     }

     private void fillRightColumn(Point[][] corners) {
          fillMissingColumnIncludingCorners(corners, 6, 7, 8);
     }

     private void fillLeftColumn(Point[][] corners) {
          fillMissingColumnIncludingCorners(corners, 2, 1, 0);
     }

     private void fillMissingColumnIncludingCorners(Point[][] corners, int previousColumn, int focusedColumn, int missingColumn) {
          for(int i = 0; i < VERTICAL_INTERSECTIONS; ++i) {
               Point focusedPoint = corners[i][focusedColumn];
               Point nextPointDown = corners[i][previousColumn];
               Vec2f vector = toVector(nextPointDown, focusedPoint);
               corners[i][missingColumn] = CoordUtil.pointMovedByVector(focusedPoint, vector);
          }
     }

     private void fillInnerCorners(MatOfPoint2f matOfInnerCorners, Point[][] fullMat) {
          Point[] innerCorners = matOfInnerCorners.toArray();
          for (int i = 0; i < CORNERS_PATTERN_HEIGHT; ++i) {
               for (int j = 0; j < CORNERS_PATTERN_WIDTH; ++j) {
                    fullMat[i+1][j+1] = innerCorners[toOneDimension(i, j, CORNERS_PATTERN_WIDTH)];
               }
          }
     }
}
