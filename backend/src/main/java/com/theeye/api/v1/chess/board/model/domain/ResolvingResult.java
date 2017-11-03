package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.enumeration.ResolvingStatus;
import lombok.Data;

@Data
public class ResolvingResult {

   private Board board;
   private ResolvingStatus resolvingStatus;
   private PlayerColor lastMoveBy;
}
