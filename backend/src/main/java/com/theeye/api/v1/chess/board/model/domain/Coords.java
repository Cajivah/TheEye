package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Coords {

     private int row;
     private int column;

     public String toInvertedChessboardString() {
          char boardColumnLetter = (char) ('a' + column);
          return boardColumnLetter + String.valueOf(BoardConsts.ROWS - row);
     }
}
