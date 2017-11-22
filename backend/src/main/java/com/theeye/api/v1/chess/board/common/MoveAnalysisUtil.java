package com.theeye.api.v1.chess.board.common;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.piece.model.domain.Piece;

import java.util.*;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.board.common.PlayerColor.BLACK;
import static com.theeye.api.v1.chess.board.common.PlayerColor.WHITE;
import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.*;

public class MoveAnalysisUtil {

     public static ChangeType findChangeType(PlayerColor activePlayer,
                                             Occupancy newOccupancy,
                                             Piece lastPiece) {
          Occupancy activePlayerOccupancy = activePlayer.equals(WHITE)
                  ? Occupancy.OCCUPIED_BY_WHITE
                  : Occupancy.OCCUPIED_BY_BLACK;

          return analyzeChange(activePlayerOccupancy, newOccupancy, lastPiece);
     }

     private static ChangeType analyzeChange(Occupancy activePlayerOccupancy,
                                             Occupancy newOccupancy,
                                             Piece lastPiece) {
          if (becameOccupiedByActive(activePlayerOccupancy, newOccupancy)) {
               return analyzeBecameOccupiedByActive(lastPiece);
          } else if (becameUnoccupied(newOccupancy)) {
               return analyzeBecameUnoccupied(lastPiece);
          } else {
               return UNKNOWN;
          }
     }

     private static boolean becameOccupiedByActive(Occupancy activePlayerOccupancy, Occupancy newOccupancy) {
          return newOccupancy.equals(activePlayerOccupancy);
     }

     private static boolean becameUnoccupied(Occupancy newOccupancy) {
          return newOccupancy.equals(Occupancy.UNOCCUPIED);
     }

     private static ChangeType analyzeBecameUnoccupied(Piece lastPiece) {
          if (lastPiece.getOwner().equals(WHITE)) {
               return OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED;
          } else {
               return OCCUPIED_BY_OPPONENT_TO_UNOCCUPIED;
          }
     }

     private static ChangeType analyzeBecameOccupiedByActive(Piece lastPiece) {
          if (lastPiece.getOwner().equals(BLACK)) {
               return OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER;
          } else {
               return UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE;
          }
     }

     public static Map<ChangeType, Integer> countChangeTypes(List<TileChange> changes) {
          final Map<ChangeType, Integer> changeTypeCount =
                  Arrays.stream(ChangeType.values())
                        .collect(Collectors.toMap(type -> type, type -> 0));
          changes.forEach(change -> incrementCount(changeTypeCount, change.getChangeType()));
          return changeTypeCount;
     }

     private static void incrementCount(Map<ChangeType, Integer> changeTypeCount, ChangeType changeType) {
          Integer count = changeTypeCount.get(changeType);
          changeTypeCount.replace(changeType, count + 1);
     }
}
