package com.theeye.api.v1.chess.engine.controller;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.engine.exception.ChessEngineException;
import com.theeye.api.v1.chess.engine.model.dto.BestMoveDTO;
import com.theeye.api.v1.chess.engine.model.dto.EvaluationScoreDTO;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import extension.SpringExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith({SpringExtension.class})
class EngineControllerTest {

     @Autowired
     private EngineController sut;

     @Test
     @DisplayName("Given fen for score analysis")
     void evaluatePosition() {
          EvaluationScoreDTO score =
                  sut.evaluatePosition(Fen.of(BoardConsts.STARTING_SET_UP_FEN));
          assertNotNull(score);

          assertThrows(ChessEngineException.class,
                  () -> sut.evaluatePosition(Fen.of("InvalidFen")));
     }

     @Test
     @DisplayName("Given fen for best move analysis")
     void findBestMove() {
          BestMoveDTO bestMove =
                  sut.findBestMove(Fen.of(BoardConsts.STARTING_SET_UP_FEN));
          assertNotNull(bestMove);

          assertThrows(ChessEngineException.class,
                  () -> sut.findBestMove(Fen.of("InvalidFen")));
     }

}