package com.theeye.api.v1.chess.board.factory;


import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import com.theeye.api.v1.chess.fen.parser.FenDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory {

     private final FenDecoder fenDecoder;

     @Autowired
     public BoardFactory(FenDecoder fenDecoder) {
          this.fenDecoder = fenDecoder;
     }

     public Board createEmptyBoard() {
          return Board.builder()
                      .tiles(TileFactory.createEmpty())
                      .build();
     }

     public Board createWithStartingPosition() {
          return fenDecoder.decode(Fen.of(BoardConsts.STARTING_SET_UP_FEN));
     }
}
