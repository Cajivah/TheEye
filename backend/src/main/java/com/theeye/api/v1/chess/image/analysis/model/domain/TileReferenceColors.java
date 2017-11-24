package com.theeye.api.v1.chess.image.analysis.model.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.opencv.core.Scalar;

@Value
@Getter
@Builder
public class TileReferenceColors {

     private Scalar occupiedByWhite;
     private Scalar occupiedByBlack;
     private Scalar unoccupied;
}
