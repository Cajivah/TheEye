package com.theeye.api.v1.chess.board.model.dto;

import com.theeye.api.v1.chess.analysis.model.dto.ChessboardPositionFeaturesDTO;
import com.theeye.api.v1.chess.analysis.model.dto.ReferenceColorsDTO;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MoveToResolveDTO {

     private ChessboardImageDTO chessboardImage;
     private ChessboardPositionFeaturesDTO positions;
     private ReferenceColorsDTO referenceColors;
     private Fen lastPosition;
}
