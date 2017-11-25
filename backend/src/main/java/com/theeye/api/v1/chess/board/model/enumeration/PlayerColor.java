package com.theeye.api.v1.chess.board.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerColor {

     WHITE(0, 1),
     BLACK(7, 6),
     NONE(-1, -1);

     private int objectiveRow;
     private int pawnRow;
}
