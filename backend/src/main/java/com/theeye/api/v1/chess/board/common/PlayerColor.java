package com.theeye.api.v1.chess.board.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerColor {

     White(2, 1),
     Black(7, 8);

     private int firstRankIndex;
     private int secondRankIndex;
}
