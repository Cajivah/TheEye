package com.theeye.api.v1.chess.engine.controller;

import com.theeye.api.v1.chess.engine.mapper.EngineMapper;
import com.theeye.api.v1.chess.engine.model.dto.BestMoveDTO;
import com.theeye.api.v1.chess.engine.model.dto.EvaluationScoreDTO;
import com.theeye.api.v1.chess.engine.service.EngineService;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chess/engine")
public class EngineController {

     private final EngineService engineService;

     private final EngineMapper engineMapper;

     @Autowired
     public EngineController(EngineService engineService,
                             EngineMapper engineMapper) {
          this.engineService = engineService;
          this.engineMapper = engineMapper;
     }

     @PostMapping("/score")
     public EvaluationScoreDTO evaluatePosition(@RequestBody Fen fen) {
          float evalScore = engineService.evaluatePositionScore(fen);
          return engineMapper.toEvaluationScoreDTO(evalScore);
     }

     @PostMapping("/move")
     public BestMoveDTO findBestMove(@RequestBody Fen fen) {
          String bestMove = engineService.findBestMove(fen);
          return  engineMapper.toBestMoveDTO(bestMove);
     }

}
