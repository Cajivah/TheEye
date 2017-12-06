package com.theeye.api.v1.chess.image.analysis.model.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InitialColorReferenceIndexes {

     public static final int[] COLUMNS_OCCUPIED_BY_WHITE = {6, 7};
     public static final int[] COLUMNS_OCCUPIED_BY_BLACK = {0, 1};
     public static final int[] COLUMNS_UNOCCUPIED = {2, 3, 4, 5};
}
