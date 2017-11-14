package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.analysis.mapper.ImageMapper;
import com.theeye.api.v1.chess.board.model.domain.ResolvedMove;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageMapper.class, BoardMapper.class})
public abstract class MoveMapper {

     @Mapping(target = "lastBoard", source = "lastPosition")
     public abstract UnresolvedMove toUnresolvedMove(MoveToResolveDTO moveToResolveDTO);

     @Mapping(target = "fen", source = "board")
     public abstract NewPositionDTO toNewPosition(ResolvedMove resolvedMove);
}