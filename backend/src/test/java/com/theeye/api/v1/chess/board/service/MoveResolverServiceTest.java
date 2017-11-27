package com.theeye.api.v1.chess.board.service;

import com.theeye.api.factory.BoardTestFactory;
import com.theeye.api.factory.OccupancyTestFactory;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.image.analysis.service.AnalysisService;
import extension.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveResolverServiceTest {

     @Mock
     private AnalysisService analysisService;
     @Mock
     private BoardService boardService;
     @Mock
     private MoveTypeService moveTypeService;

     @InjectMocks
     private MoveResolverService sut;


     @Test
     void determineNewState() {
          Board lastState = BoardTestFactory.createInitialBoard();
          Occupancy[][] newOccupancy = OccupancyTestFactory.createAfterKingSideCastling();

          when(moveTypeService.findMoveType(any())).thenReturn(MoveType.UNKNOWN);

          assertThrows(
                  MoveDetectionException.class,
                  ()-> sut.determineNewState(lastState, newOccupancy));
     }
}