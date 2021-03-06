package com.theeye.api.v1.chess.image.analysis.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChessboardPositionFeaturesDTO {

     PointDTO[] chessboardCorners;
     PointDTO[][] tilesCornerPoints;
}
