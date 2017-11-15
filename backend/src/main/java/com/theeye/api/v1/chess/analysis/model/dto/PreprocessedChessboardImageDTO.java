package com.theeye.api.v1.chess.analysis.model.dto;

import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class PreprocessedChessboardImageDTO {

     @NotNull
     ChessboardImageDTO image;

     @NotNull
     PointDTO[][] tilesCornerPoints;

     @NotNull
     PointDTO[] chessboardCorners;
}
