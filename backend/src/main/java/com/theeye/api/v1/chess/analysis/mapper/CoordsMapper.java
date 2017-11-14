package com.theeye.api.v1.chess.analysis.mapper;

import com.theeye.api.v1.chess.analysis.model.domain.TileCorners;
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
}
