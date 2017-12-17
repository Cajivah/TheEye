package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.image.analysis.mapper.ColorMapper;
import com.theeye.api.v1.chess.image.analysis.mapper.CoordsMapper;
import com.theeye.api.v1.chess.image.analysis.mapper.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ImageMapper.class,
                BoardMapper.class,
                CoordsMapper.class,
                ColorMapper.class
        }
)
public abstract class MoveMapper {

     @Mapping(target = "lastBoard", source = "lastPosition")
     @Mapping(target = "chessboardCorners", source = "positions.chessboardCorners")
     @Mapping(target = "tilesCorners", source = "positions.tilesCornerPoints")
     public abstract UnresolvedMove toUnresolvedMove(MoveToResolveDTO moveToResolveDTO);
}