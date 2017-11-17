package com.theeye.api.v1.chess.board.utils;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;

import java.util.function.Predicate;

public class BoardPredicates {

     public static final Predicate<TileChange> CHANGED_TO_UNOCCUPIED =
             change -> change.getNewOccupancy().equals(Occupancy.UNOCCUPIED);

     public static final Predicate<TileChange> ACTIVE_MOVE_CHANGES =
             change -> change.getChangeType().equals(ChangeType.OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                     || change.getChangeType().equals(ChangeType.UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE);

     public static final Predicate<TileChange> UNOCCUPIED_BY_OPPONENT =
             change -> change.getChangeType().equals(ChangeType.OCCUPIED_BY_OPPONENT_TO_UNOCCUPIED);

     public static final Predicate<MoveType> WAS_CASTLING =
             moveType -> moveType.equals(MoveType.CASTLE_KING) || moveType.equals(MoveType.CASTLE_QUEEN);

     public static final Predicate<MoveType> WAS_REGULAR_OR_TAKE =
             moveType -> moveType.equals(MoveType.REGULAR) || moveType.equals(MoveType.TAKE);

     public static final Predicate<PieceType> IS_KING =
             piece -> piece != null && (piece.equals(PieceType.KING_BLACK) || piece.equals(PieceType.KING_WHITE));

     public static final Predicate<Piece> IS_KING_PIECE =
             piece -> piece != null
                     && (piece.getPieceType().equals(PieceType.KING_BLACK)
                     || piece.getPieceType().equals(PieceType.KING_WHITE));

     public static final Predicate<PieceType> IS_ROOK =
             piece -> piece != null && (piece.equals(PieceType.ROOK_BLACK) || piece.equals(PieceType.ROOK_WHITE));

     public static final Predicate<Piece> IS_ROOK_PIECE =
             piece -> piece != null
                     && (piece.getPieceType().equals(PieceType.ROOK_BLACK)
                     || piece.getPieceType().equals(PieceType.ROOK_WHITE));

}


