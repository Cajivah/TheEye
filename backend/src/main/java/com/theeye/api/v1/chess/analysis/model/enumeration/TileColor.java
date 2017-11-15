package com.theeye.api.v1.chess.analysis.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TileColor {

     White(1),
     Black(0)
     ;

     private int offset;
}
