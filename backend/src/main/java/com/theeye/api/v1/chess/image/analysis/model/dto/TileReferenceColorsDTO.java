package com.theeye.api.v1.chess.image.analysis.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TileReferenceColorsDTO {

     private RGBColorDTO occupiedByWhite;
     private RGBColorDTO occupiedByBlack;
     private RGBColorDTO unoccupied;
}
