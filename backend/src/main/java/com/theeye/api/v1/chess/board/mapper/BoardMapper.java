package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import com.theeye.api.v1.chess.fen.parser.FenDecoder;
import com.theeye.api.v1.chess.fen.parser.FenEncoder;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BoardMapper {

     private FenDecoder fenDecoder;
     private FenEncoder fenEncoder;

     @Autowired
     public void setFenDecoder(FenDecoder fenDecoder,
                               FenEncoder fenEncoder) {
          this.fenDecoder = fenDecoder;
          this.fenEncoder = fenEncoder;
     }

     public Board toBoard(Fen fen) {
          return fenDecoder.decode(fen);
     }

     public Fen toFEN(Board board) {
          return fenEncoder.encode(board);
     }
}
