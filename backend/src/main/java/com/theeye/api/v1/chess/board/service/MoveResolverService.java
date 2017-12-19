package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.image.analysis.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveResolverService {

     private final AnalysisService analysisService;
     private final BoardService boardService;
     private final MoveTypeService moveTypeService;

     @Autowired
     public MoveResolverService(AnalysisService analysisService,
                                MoveTypeService moveTypeService,
                                BoardService boardService) {
          this.analysisService = analysisService;
          this.moveTypeService = moveTypeService;
          this.boardService = boardService;
     }

     public Board resolveMove(UnresolvedMove unresolvedMove) {

          Occupancy[][] tilesOccupancy = analysisService.getChessboardOccupancy(unresolvedMove);
          return determineNewState(unresolvedMove.getLastBoard(), tilesOccupancy);
     }

     public Board determineNewState(Board lastState, Occupancy[][] newOccupancy) {
          PlayerColor activeColor = lastState.getActiveColor();
          List<TileChange> changedTiles = moveTypeService.findChangedTiles(lastState.getTiles(), newOccupancy, activeColor);
          MoveType moveType = moveTypeService.findMoveType(changedTiles);
          if (moveType.equals(MoveType.UNKNOWN)) {
               throw new MoveDetectionException();
          }
          return boardService.doMove(lastState, changedTiles, moveType);
     }
}
