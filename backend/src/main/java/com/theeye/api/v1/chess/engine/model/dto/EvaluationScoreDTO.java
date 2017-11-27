package com.theeye.api.v1.chess.engine.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EvaluationScoreDTO {

     private float centipawnScore;
}
