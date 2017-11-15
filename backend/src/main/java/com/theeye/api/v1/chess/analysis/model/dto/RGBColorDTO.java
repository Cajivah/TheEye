package com.theeye.api.v1.chess.analysis.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RGBColorDTO {

     private int red;
     private int green;
     private int blue;
}
