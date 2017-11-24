package com.theeye.api.v1.chess.board.controller;

import com.theeye.api.v1.chess.board.mapper.BoardMapper;
import com.theeye.api.v1.chess.board.mapper.MoveMapper;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import com.theeye.api.v1.chess.board.model.dto.EvaluationScoreDTO;
import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.board.service.MoveResolverService;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chess/board")
public class BoardController {

     private final MoveResolverService moveResolverService;
     private final MoveMapper moveMapper;
     private final BoardMapper boardMapper;

     @Autowired
     public BoardController(MoveResolverService moveResolverService,
                            MoveMapper moveMapper,
                            BoardMapper boardMapper) {
          this.moveResolverService = moveResolverService;
          this.moveMapper = moveMapper;
          this.boardMapper = boardMapper;
     }

     @PostMapping("/move")
     public Fen findNewPosition(@RequestBody MoveToResolveDTO moveToResolve) {
          UnresolvedMove unresolvedMove = moveMapper.toUnresolvedMove(moveToResolve);
          Board newState = moveResolverService.resolveMove(unresolvedMove);
          return boardMapper.toFEN(newState);
     }

     @PostMapping("/evaluation")
     public EvaluationScoreDTO evaluatePosition(@RequestBody Fen fen) {
          return null;
     }
}
