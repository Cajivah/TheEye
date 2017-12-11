package com.theeye.api.v1.chess.image.analysis.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TileColor {

     WHITE(0),
     BLACK(1);

     private int offset;
}
