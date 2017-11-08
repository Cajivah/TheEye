package com.theeye.api.v1.chess.board.controller;

import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import com.theeye.api.v1.chess.board.service.MoveResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chess/board")
public class BoardController {

     private final MoveResolverService moveResolverService;

     @Autowired
     public BoardController(MoveResolverService moveResolverService) {
          this.moveResolverService = moveResolverService;
     }

     @PostMapping("/resolve")
     public NewPositionDTO findNewPosition(@RequestBody MoveToResolveDTO moveToResolve) {
          return null;
     }
}
