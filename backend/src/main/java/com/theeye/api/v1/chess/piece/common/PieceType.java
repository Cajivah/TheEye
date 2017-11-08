package com.theeye.api.v1.chess.piece.common;

import com.theeye.api.v1.chess.piece.model.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

import static com.theeye.api.v1.chess.board.common.PlayerColor.*;

@AllArgsConstructor
@Getter
public enum PieceType {

     PAWN_WHITE(() -> Pawn.of(White)),
     PAWN_BLACK(() -> Pawn.of(Black)),
     ROOK_WHITE(() -> Rook.of(White)),
     ROOK_BLACK(() -> Rook.of(Black)),
     KNIGHT_WHITE(() -> Knight.of(White)),
     KNIGHT_BLACK(() -> Knight.of(Black)),
     BISHOP_WHITE(() -> Bishop.of(White)),
     BISHOP_BLACK(() -> Bishop.of(Black)),
     QUEEN_WHITE(() -> Queen.of(White)),
     QUEEN_BLACK(() -> Queen.of(Black)),
     KING_WHITE(() -> King.of(White)),
     KING_BLACK(() -> King.of(Black)),
     EMPTY(() -> Empty.of(None));

     private final Supplier<Piece> pieceSupplier;

     public Piece create() {
          return getPieceSupplier().get();
     }
}
