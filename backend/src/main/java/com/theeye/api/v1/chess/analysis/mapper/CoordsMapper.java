package com.theeye.api.v1.chess.analysis.mapper;

import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
import com.theeye.api.v1.chess.analysis.model.dto.ChessBoardFeaturesDTO;
import com.theeye.api.v1.chess.analysis.model.dto.PointDTO;
import com.theeye.api.v1.chess.board.common.BoardConsts;
import org.mapstruct.Mapper;
import org.opencv.core.Point;

@Mapper(componentModel = "spring")
public class CoordsMapper {

     public TileCorners[][] toTilesCoords(Point[][] corners) {
          TileCorners[][] tileCorners = new TileCorners[BoardConsts.ROWS][BoardConsts.COLUMNS];
          for(int i = 0; i < BoardConsts.ROWS; ++i) {
               for (int j = 0; j < BoardConsts.COLUMNS; ++j) {
                    tileCorners[i][j] = TileCorners.builder()
                                                   .topRight(corners[i][j+1])
                                                   .bottomRight(corners[i+1][j+1])
                                                   .bottomLeft(corners[i+1][j])
                                                   .topLeft(corners[i][j])
                                                   .build();
               }
          }
          return tileCorners;
     }

     public PointDTO toPointDTO(Point point) {
          return PointDTO.builder()
                         .x((int)point.x)
                         .y((int)point.y)
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
          for(int i = 0; i < rows; ++i) {
                pointDTOs[i] = toPointsDTO(points[i]);
          }
          return pointDTOs;
     }

     public ChessBoardFeaturesDTO toChessboardFeaturesDTO(Point[] roiCorners, Point[][] tileCornerPoints) {
          return ChessBoardFeaturesDTO.builder()
                                      .chessboardCorners(toPointsDTO(roiCorners))
                                      .tilesCornerPoints(toPointsDTO(tileCornerPoints))
                                      .build();
     }
}
