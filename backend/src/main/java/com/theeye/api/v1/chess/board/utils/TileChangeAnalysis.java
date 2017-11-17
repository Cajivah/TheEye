package com.theeye.api.v1.chess.board.utils;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.piece.model.domain.Piece;

import java.util.List;

import static com.theeye.api.v1.chess.board.utils.BoardPredicates.CHANGED_TO_UNOCCUPIED;
import static com.theeye.api.v1.chess.board.utils.BoardPredicates.IS_KING;

public class TileChangeAnalysis {

     public static boolean movedKing(List<TileChange> tileChanges) {
          return tileChanges.stream()
                            .filter(CHANGED_TO_UNOCCUPIED)
                            .map(TileChange::getLastPiece)
                            .map(Piece::getPieceType)
                            .anyMatch(IS_KING);
     }

     public static boolean movedKingSideRook(List<TileChange> changes, PlayerColor activePlayer) {
          Coords coords = Coords.builder()
                                .row(BoardConsts.KING_SIDE_ROOK_COLUMN_INDEX)
                                .column(activePlayer.getObjectiveRow())
                                .build();
          return movedFromCoords(changes, coords);
     }

     public static boolean movedQueenSideRook(List<TileChange> changes, PlayerColor activePlayer) {
          Coords coords = Coords.builder()
                                .row(BoardConsts.QUEEN_SIDE_ROOK_COLUMN_INDEX)
                                .column(activePlayer.getObjectiveRow())
                                .build();
          return movedFromCoords(changes, coords);
     }

     private static boolean movedFromCoords(List<TileChange> changes, Coords tileCoords) {
          return changes.stream()
                        .filter(CHANGED_TO_UNOCCUPIED)
                        .map(TileChange::getCoords)
                        .anyMatch(coords -> coords.equals(tileCoords));
     }
}
