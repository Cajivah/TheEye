package com.theeye.api.v1.chess.piece.model.enumeration;

import com.theeye.api.v1.chess.piece.model.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.*;

@AllArgsConstructor
@Getter
public enum PieceType {

     PAWN_WHITE(() -> Pawn.of(WHITE)),
     PAWN_BLACK(() -> Pawn.of(BLACK)),
     ROOK_WHITE(() -> Rook.of(WHITE)),
     ROOK_BLACK(() -> Rook.of(BLACK)),
     KNIGHT_WHITE(() -> Knight.of(WHITE)),
     KNIGHT_BLACK(() -> Knight.of(BLACK)),
     BISHOP_WHITE(() -> Bishop.of(WHITE)),
     BISHOP_BLACK(() -> Bishop.of(BLACK)),
     QUEEN_WHITE(() -> Queen.of(WHITE)),
     QUEEN_BLACK(() -> Queen.of(BLACK)),
     KING_WHITE(() -> King.of(WHITE)),
     KING_BLACK(() -> King.of(BLACK)),
     EMPTY(() -> Empty.of(NONE));

     private final Supplier<Piece> pieceSupplier;

     public Piece create() {
          return getPieceSupplier().get();
     }
}
