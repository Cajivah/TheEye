package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.analysis.service.AnalysisService;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.moveresolver.MoveResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveResolverService {

     private final AnalysisService analysisService;
     private final BoardService boardService;
     private final MoveResolver moveResolver;

     @Autowired
     public MoveResolverService(AnalysisService analysisService,
                                MoveResolver moveResolver,
                                BoardService boardService) {
          this.analysisService = analysisService;
          this.moveResolver = moveResolver;
          this.boardService = boardService;
     }

     public Board resolveMove(UnresolvedMove unresolvedMove) {

          Occupancy[][] tilesOccupancy = analysisService.getChessboardOccupancy(unresolvedMove);
          return determineNewState(unresolvedMove.getLastBoard(), tilesOccupancy);
     }

     public Board determineNewState(Board lastState, Occupancy[][] newOccupancy) {
          PlayerColor activeColor = lastState.getActiveColor();
          List<TileChange> changedTiles = moveResolver.findChangedTiles(lastState.getTiles(), newOccupancy, activeColor);
          MoveType moveType = moveResolver.findMoveType(changedTiles);
          if (moveType.equals(MoveType.UNKNOWN)) {
               throw new MoveDetectionException();
          }
          return boardService.doMove(lastState, changedTiles, moveType);
     }
}
