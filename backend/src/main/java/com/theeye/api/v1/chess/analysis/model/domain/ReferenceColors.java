package com.theeye.api.v1.chess.analysis.model.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReferenceColors {

     TileReferenceColors whiteTiles;
     TileReferenceColors blackTiles;
}
