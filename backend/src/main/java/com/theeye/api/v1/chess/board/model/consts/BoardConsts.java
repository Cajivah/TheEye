package com.theeye.api.v1.chess.board.model.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardConsts {

     public final static Integer ROWS = 8;
     public final static Integer COLUMNS = 8;

     public final static Integer QUEEN_SIDE_ROOK_COLUMN_INDEX = 0;
     public final static Integer KING_SIDE_ROOK_COLUMN_INDEX = 7;

     public final static String STARTING_SET_UP_FEN =
             "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
}
