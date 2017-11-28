package com.theeye.api.v1.chess.engine.service;

import com.theeye.api.v1.chess.engine.evaluation.Stockfish;
import com.theeye.api.v1.chess.engine.exception.ChessEngineException;
import com.theeye.api.v1.chess.engine.properties.StockfishProperties;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import io.vavr.control.Try;
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
          return Try.of(Stockfish::new)
                    .mapTry(stockfish ->
                            stockfish.startEngine(stockfishProperties.getPath()))
                    .mapTry(stockfish ->
                            stockfish.getEvalScoreAndClose(
                                    fen.getFenDescription(),
                                    stockfishProperties.getTime()))
                    .getOrElseThrow(ChessEngineException::of);
     }

     public String findBestMove(Fen fen) {
          return Try.of(Stockfish::new)
                    .mapTry(stockfish ->
                            stockfish.startEngine(stockfishProperties.getPath()))
                    .mapTry(stockfish ->
                            stockfish.getBestMoveAndClose(
                                    fen.getFenDescription(),
                                    stockfishProperties.getTime()))
                    .getOrElseThrow(ChessEngineException::of);
     }
}
