package com.theeye.api.v1.chess.board.model.dto;

import com.theeye.api.v1.chess.board.model.domain.Coords;
import lombok.Value;

@Value
public class MoveDTO {

   private Coords from;
   private Coords to;
}
