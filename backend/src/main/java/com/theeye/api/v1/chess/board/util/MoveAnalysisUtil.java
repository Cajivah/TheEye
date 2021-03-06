package com.theeye.api.v1.chess.board.util;

import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.piece.model.domain.Piece;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.*;
import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.NONE;
import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.WHITE;

public class MoveAnalysisUtil {

     public static ChangeType findChangeType(PlayerColor activePlayer,
                                             Occupancy newOccupancy,
                                             Piece lastPiece) {


          return analyzeChange(activePlayer, newOccupancy, lastPiece);
     }

     private static ChangeType analyzeChange(PlayerColor activePlayer,
                                             Occupancy newOccupancy,
                                             Piece lastPiece) {

          Occupancy activePlayerOccupancy = activePlayer.equals(WHITE)
                  ? Occupancy.OCCUPIED_BY_WHITE
                  : Occupancy.OCCUPIED_BY_BLACK;

          if (becameOccupiedByActive(activePlayerOccupancy, newOccupancy)) {
               return analyzeBecameOccupiedByActive(lastPiece);
          } else if (becameUnoccupied(newOccupancy)) {
               return analyzeBecameUnoccupied(activePlayer, lastPiece);
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

     private static ChangeType analyzeBecameUnoccupied(PlayerColor activePlayer, Piece lastPiece) {
          if (lastPiece.getOwner().equals(activePlayer)) {
               return OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED;
          } else {
               return OCCUPIED_BY_OPPONENT_TO_UNOCCUPIED;
          }
     }

     private static ChangeType analyzeBecameOccupiedByActive(Piece lastPiece) {
          if (lastPiece.getOwner().equals(NONE)) {
               return UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE;
          } else {
               return OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER;
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
