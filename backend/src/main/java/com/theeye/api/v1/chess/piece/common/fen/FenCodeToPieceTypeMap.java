package com.theeye.api.v1.chess.piece.common.fen;

import com.google.common.collect.ImmutableMap;
import com.theeye.api.v1.chess.piece.common.PieceType;
import com.theeye.api.v1.chess.piece.exception.InvalidFenException;

import java.util.Map;
import java.util.Optional;

public class FenCodeToPieceTypeMap {
   public static Map<Character, PieceType> fenToPiece =
           ImmutableMap.<Character, PieceType>builder()
                   .put(FenCodes.EMPTY, null)
                   .put(FenCodes.ROOK_BLACK, PieceType.ROOK_BLACK)
                   .put(FenCodes.ROOK_WHITE, PieceType.ROOK_WHITE)
                   .put(FenCodes.BISHOP_BLACK, PieceType.BISHOP_BLACK)
                   .put(FenCodes.BISHOP_WHITE, PieceType.BISHOP_WHITE)
                   .put(FenCodes.KNIGHT_BLACK, PieceType.KNIGHT_BLACK)
                   .put(FenCodes.KNIGHT_WHITE, PieceType.KNIGHT_WHITE)
                   .put(FenCodes.QUEEN_BLACK, PieceType.QUEEN_BLACK)
                   .put(FenCodes.QUEEN_WHITE, PieceType.QUEEN_WHITE)
                   .put(FenCodes.KING_BLACK, PieceType.KING_BLACK)
                   .put(FenCodes.KING_WHITE, PieceType.KING_WHITE)
                   .put(FenCodes.PAWN_BLACK, PieceType.PAWN_BLACK)
                   .put(FenCodes.PAWN_WHITE, PieceType.PAWN_WHITE)
                   .build();

   public static PieceType forFen(char fen) {
      return Optional.ofNullable(fenToPiece.get(fen))
                     .orElseThrow(() -> InvalidFenException.of(fen));
   }
}
