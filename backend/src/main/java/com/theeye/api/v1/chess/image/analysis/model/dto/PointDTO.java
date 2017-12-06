package com.theeye.api.v1.chess.image.analysis.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class PointDTO {
     int x;
     int y;
}
