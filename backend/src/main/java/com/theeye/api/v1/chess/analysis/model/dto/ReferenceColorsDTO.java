package com.theeye.api.v1.chess.analysis.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReferenceColorsDTO {

     private TileReferenceColorsDTO whiteTiles;
     private TileReferenceColorsDTO blackTiles;
}
