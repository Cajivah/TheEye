package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.analysis.mapper.ImageMapper;
import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.analysis.service.AnalysisService;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MoveResolverService {

     private final ImageMapper imageMapper;
     private final AnalysisService analysisService;

     @Autowired
     public MoveResolverService(ImageMapper imageMapper,
                                AnalysisService analysisService) {
          this.imageMapper = imageMapper;
          this.analysisService = analysisService;
     }

     public Board resolveMove(UnresolvedMove unresolvedMove) throws IOException {

          Occupancy[][] tilesOccupancy = analysisService.getChessboardOccupancy(unresolvedMove);
          return determineNewState(unresolvedMove.getLastBoard(), tilesOccupancy);
     }

     public Board determineNewState(Board lastState, Occupancy[][] newState) {
          return null;
     }
}
