package com.theeye.api.v1.chess.board.common;

public enum PossibleMoveResult {
     OK,
     INVALID,
     PIECE_TAKEN,
     CHECK,
     CHECKMATE,
     EN_PASSANT,
     KING_CASTLE,
     QUEEN_CASTLE
}
