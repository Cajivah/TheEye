package com.theeye.api.v1.chess.analysis.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TileColor {

     WHITE(1),
     BLACK(0)
     ;

     private int offset;
}
