package com.theeye.api.v1.chess.board.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coords {

     private int row;
     private int column;

     public String toInvertedChessboardString() {
          char boardColumnLetter = (char) ('a' + row);
          return boardColumnLetter + String.valueOf(column + 1);
     }
}
