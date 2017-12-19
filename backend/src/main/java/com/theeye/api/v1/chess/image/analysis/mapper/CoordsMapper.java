package com.theeye.api.v1.chess.image.analysis.mapper;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.image.analysis.model.dto.ChessboardPositionFeaturesDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.PointDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.mapstruct.Mapper;
import org.opencv.core.Point;

@Mapper(componentModel = "spring")
public class CoordsMapper {

     public TileCorners[][] toTilesCoords(Point[][] corners) {
          TileCorners[][] tileCorners = new TileCorners[BoardConsts.ROWS][BoardConsts.COLUMNS];
          for (int i = 0; i < BoardConsts.ROWS; ++i) {
               for (int j = 0; j < BoardConsts.COLUMNS; ++j) {
                    tileCorners[i][j] = TileCorners.builder()
                                                   .topRight(corners[i][j + 1])
                                                   .bottomRight(corners[i + 1][j + 1])
                                                   .bottomLeft(corners[i + 1][j])
                                                   .topLeft(corners[i][j])
                                                   .build();
               }
          }
          return tileCorners;
     }

     public PointDTO toPointDTO(Point point) {
          return PointDTO.builder()
                         .x((int) point.x)
                         .y((int) point.y)
                         .build();
     }

     public PointDTO[] toPointsDTO(Point[] points) {
          int count = points.length;
          PointDTO[] pointDTOs = new PointDTO[count];
          for (int i = 0; i < count; ++i) {
               pointDTOs[i] = toPointDTO(points[i]);
          }
          return pointDTOs;
     }

     public PointDTO[][] toPointsDTO(Point[][] points) {
          int rows = points.length;
          PointDTO[][] pointDTOs = new PointDTO[rows][];
          for (int i = 0; i < rows; ++i) {
               pointDTOs[i] = toPointsDTO(points[i]);
          }
          return pointDTOs;
     }

     public ChessboardPositionFeaturesDTO toChessboardFeaturesDTO(Point[] roiCorners, Point[][] tileCornerPoints) {

          if(tileCornerPoints[0][0].y > tileCornerPoints[1][0].y) {
               reverseInnerCorners(tileCornerPoints);
          }
          return ChessboardPositionFeaturesDTO.builder()
                                              .chessboardCorners(toPointsDTO(roiCorners))
                                              .tilesCornerPoints(toPointsDTO(tileCornerPoints))
                                              .build();
     }

     private void reverseInnerCorners(Point[][] tileCornerPoints) {
          ArrayUtils.reverse(tileCornerPoints);
          for (Point[] points : tileCornerPoints) {
               ArrayUtils.reverse(points);
          }
     }

     public TileCorners[][] toTilesCoords(PointDTO[][] tilesCornerPoints) {
          Point[][] points = toPoints(tilesCornerPoints);
          return toTilesCoords(points);
     }

     public Point[][] toPoints(PointDTO[][] pointDTOs) {
          int rows = pointDTOs.length;
          Point[][] points = new Point[rows][];
          for (int i = 0; i < rows; ++i) {
               points[i] = toPoints(pointDTOs[i]);
          }
          return points;
     }

     public Point[] toPoints(PointDTO[] pointDTOs) {
          if(pointDTOs == null) {
               return null;
          }
          int count = pointDTOs.length;
          Point[] points = new Point[count];
          for (int i = 0; i < count; ++i) {
               points[i] = toPoint(pointDTOs[i]);
          }
          return points;
     }

     private Point toPoint(PointDTO pointDTO) {
          return new Point(pointDTO.getX(), pointDTO.getY());
     }

     public ChessboardPositionFeaturesDTO toChessboardFeaturesDTO(Point[][] points) {
          if(points[0][0].y > points[1][0].y) {
               reverseInnerCorners(points);
          }
          return ChessboardPositionFeaturesDTO.builder()
                                              .tilesCornerPoints(toPointsDTO(points))
                                              .chessboardCorners(null)
                                              .build();
     }
}
