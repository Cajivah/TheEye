package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.board.mapper.ImageMapper;
import com.theeye.api.v1.chess.board.model.domain.ResolvedMove;
import com.theeye.api.v1.chess.board.model.domain.UnresolvedMove;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveResolverService {

     private final ImageMapper imageMapper;

     @Autowired
     public MoveResolverService(ImageMapper imageMapper) {
          this.imageMapper = imageMapper;
     }

     public ResolvedMove resolveMove(UnresolvedMove unresolvedMove) {
          Mat mat = imageMapper.toMat(unresolvedMove.getChessboardImage().getImage());
          return null;
     }
}
