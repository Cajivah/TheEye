package com.theeye.api.v1.chess.board.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResolvedMoveDTO {

     String newPosition;
     String move;
}
