package com.theeye.api.v1.chess.board.controller;

import com.theeye.api.v1.chess.analysis.service.AnalysisService;
import com.theeye.api.v1.chess.board.mapper.MoveMapper;
import com.theeye.api.v1.chess.board.model.domain.ResolvedMove;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import com.theeye.api.v1.chess.board.service.MoveResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/chess/board")
public class BoardController {

     private final MoveResolverService moveResolverService;
     private final AnalysisService analysisService;
     private final MoveMapper moveMapper;

     @Autowired
     public BoardController(MoveResolverService moveResolverService,
                            MoveMapper moveMapper,
                            AnalysisService analysisService) {
          this.moveResolverService = moveResolverService;
          this.moveMapper = moveMapper;
          this.analysisService = analysisService;
     }

     @PostMapping("/resolve")
     public NewPositionDTO findNewPosition(@RequestBody MoveToResolveDTO moveToResolve) throws IOException {
          UnresolvedMove unresolvedMove = moveMapper.toUnresolvedMove(moveToResolve);
          ResolvedMove resolvedMove = moveResolverService.resolveMove(unresolvedMove);
          return moveMapper.toNewPosition(resolvedMove);
     }


}
