package com.theeye.api.v1.chess.engine.service;

import com.theeye.api.v1.chess.engine.evaluation.Stockfish;
import com.theeye.api.v1.chess.engine.properties.StockfishProperties;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(StockfishProperties.class)
public class EngineService {

     private final StockfishProperties stockfishProperties;

     @Autowired
     public EngineService(StockfishProperties stockfishProperties) {
          this.stockfishProperties = stockfishProperties;
     }

     public float evaluatePositionScore(Fen fen) {
          Stockfish stockfish = new Stockfish();
          boolean b = stockfish.startEngine(stockfishProperties.getPath());
          float evalScore =
                  stockfish.getEvalScore(fen.getFenDescription(), stockfishProperties.getTime());
          stockfish.stopEngine();
          return evalScore;
     }
}
