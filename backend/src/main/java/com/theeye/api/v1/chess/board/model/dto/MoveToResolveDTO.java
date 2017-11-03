package com.theeye.api.v1.chess.board.model.dto;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.model.domain.Fen;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MoveToResolveDTO {

   private ChessboardImageDTO chessboardImage;
   private Fen lastPosition;
   private PlayerColor player;
}
