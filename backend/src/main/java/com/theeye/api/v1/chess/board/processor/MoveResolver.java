package com.theeye.api.v1.chess.board.processor;

import com.theeye.api.v1.chess.board.common.MoveAnalysisUtil;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.*;

@Component
public class MoveResolver {

     public MoveType findMoveType(List<TileChange> changes) {
          MoveType moveType;
          Map<ChangeType, Integer> changeTypeCount = MoveAnalysisUtil.countChangeTypes(changes);

          if (isRegularMove(changeTypeCount)) {
               moveType = MoveType.REGULAR;
          } else if (isTakeMove(changeTypeCount)) {
               moveType = MoveType.TAKE;
          } else if (isCastling(changeTypeCount)) {
               moveType = findCastlingSide(changes);
          } else if (isEnPassant(changeTypeCount)) {
               moveType = MoveType.EN_PASSANT;
          } else {
               moveType = MoveType.UNKNOWN;
          }
          return moveType;
     }

     private boolean isEnPassant(Map<ChangeType, Integer> changeTypeListMap) {
          //todo add position validation
          return changeTypeListMap.size() == 3 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 1 &&
                  changeTypeListMap.get(OCCUPIED_BY_OPPONENT_TO_UNOCCUPIED) == 1 &&
                  changeTypeListMap.get(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE) == 1;
     }

     private MoveType findCastlingSide(List<TileChange> changes) {
          int kingSideCastlingKingRow = 6;
          int kingSideCastlingRookRow = 5;
          boolean kingSideCastling = true;
          List<TileChange> newlyOccupied =
                  changes.stream()
                         .filter(change -> change.getChangeType().equals(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE))
                         .collect(Collectors.toList());
          for (TileChange tileChange : newlyOccupied) {
               int row = tileChange.getCoords().getRow();
               kingSideCastling = row == kingSideCastlingKingRow || row == kingSideCastlingRookRow;
          }

          return kingSideCastling ? MoveType.CASTLE_KING : MoveType.CASTLE_QUEEN;
     }

     private boolean isCastling(Map<ChangeType, Integer> changeTypeListMap) {
          return changeTypeListMap.size() == 4 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 2 &&
                  changeTypeListMap.get(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE) == 2;
     }

     private boolean isRegularMove(Map<ChangeType, Integer> changeTypeListMap) {
          return changeTypeListMap.size() == 2 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 1 &&
                  changeTypeListMap.get(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE) == 1;
     }

     private boolean isTakeMove(Map<ChangeType, Integer> changeTypeListMap) {
          return changeTypeListMap.size() == 2 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 1 &&
                  changeTypeListMap.get(OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER) == 1;
     }
}
