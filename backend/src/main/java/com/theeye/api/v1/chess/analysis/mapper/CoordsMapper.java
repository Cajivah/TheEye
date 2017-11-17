package com.theeye.api.v1.chess.analysis.mapper;

import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.analysis.model.dto.ChessboardPositionFeaturesDTO;
import com.theeye.api.v1.chess.analysis.model.dto.PointDTO;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
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
          return ChessboardPositionFeaturesDTO.builder()
                                              .chessboardCorners(toPointsDTO(roiCorners))
                                              .tilesCornerPoints(toPointsDTO(tileCornerPoints))
                                              .build();
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
}
