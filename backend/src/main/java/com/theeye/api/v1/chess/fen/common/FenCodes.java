package com.theeye.api.v1.chess.fen.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FenCodes {

     public static final String SECTION_DELIMITER = " ";
     public static final String ROW_DELIMITER = "/";
     public static final int POSITIONS_INDEX = 0;
     public static final int ACTIVE_COLOUR_INDEX = 1;
     public static final int CASTLING_INDEX = 2;
     public static final int EN_PASSANT_INDEX = 3;
     public static final int HALFMOVE_INDEX = 4;
     public static final int FULLMOVE_INDEX = 5;

     public static final String BLACK_ACTIVE = "b";
     public static final String WHITE_ACTIVE = "w";

     public static final String EMPTY = "-";

     public static final char ROOK_BLACK = 'r';
     public static final char ROOK_WHITE = 'R';
     public static final char BISHOP_BLACK = 'b';
     public static final char BISHOP_WHITE = 'B';
     public static final char KNIGHT_BLACK = 'n';
     public static final char KNIGHT_WHITE = 'N';
     public static final char KING_BLACK = 'k';
     public static final char KING_WHITE = 'K';
     public static final char QUEEN_BLACK = 'q';
     public static final char QUEEN_WHITE = 'Q';
     public static final char PAWN_BLACK = 'p';
     public static final char PAWN_WHITE = 'P';
     public static final char NO_PIECE = '1';
}
