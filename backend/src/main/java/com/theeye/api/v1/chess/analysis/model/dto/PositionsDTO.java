package com.theeye.api.v1.chess.analysis.model.dto;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PositionsDTO {

     private Occupancy[][] occupancies;
}
