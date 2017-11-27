package com.theeye.api.v1.chess.engine.mapper;

import com.theeye.api.v1.chess.engine.model.dto.BestMoveDTO;
import com.theeye.api.v1.chess.engine.model.dto.EvaluationScoreDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EngineMapper {

     public EvaluationScoreDTO toEvaluationScoreDTO(float evaluationScore) {
          return EvaluationScoreDTO.builder()
                                   .centipawnScore(evaluationScore)
                                   .build();
     }

     public BestMoveDTO toBestMoveDTO(String move) {
          return BestMoveDTO.builder()
                            .move(move)
                            .build();
     }
}
