package com.theeye.api.v1.chess.piece.common;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.piece.model.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@AllArgsConstructor
@Getter
public enum PieceType {

   PAWN_WHITE(() -> new Pawn(PlayerColor.White)),
   PAWN_BLACK(() -> new Pawn(PlayerColor.Black)),
   ROOK_WHITE(() -> new Rook(PlayerColor.White)),
   ROOK_BLACK(() -> new Rook(PlayerColor.Black)),
   KNIGHT_WHITE(() -> new Knight(PlayerColor.White)),
   KNIGHT_BLACK(() -> new Knight(PlayerColor.Black)),
   BISHOP_WHITE(() -> new Bishop(PlayerColor.White)),
   BISHOP_BLACK(() -> new Bishop(PlayerColor.Black)),
   QUEEN_WHITE(() -> new Queen(PlayerColor.White)),
   QUEEN_BLACK(() -> new Queen(PlayerColor.Black)),
   KING_WHITE(() -> new King(PlayerColor.White)),
   KING_BLACK(() -> new King(PlayerColor.Black));

   private final Supplier<Piece> pieceSupplier;
}
