package com.theeye.api.v1.chess.board.service;


import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.ResolvingResult;
import com.theeye.api.v1.chess.board.model.dto.MoveDTO;
import com.theeye.api.v1.chess.board.model.dto.MoveToResolveDTO;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import org.springframework.stereotype.Service;

@Service
public class MoveResolverService {

   public ResolvingResult resolveMove(MoveToResolveDTO moveToResolveDTO) {
       return null;
   }
}
