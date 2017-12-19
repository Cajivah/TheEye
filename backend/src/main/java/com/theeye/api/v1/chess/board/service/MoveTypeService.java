package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.board.util.MoveAnalysisUtil;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.*;

@Service
public class MoveTypeService {

     public MoveType findMoveType(List<TileChange> changes) {
          MoveType moveType;
          Map<ChangeType, Integer> changeTypeCount = MoveAnalysisUtil.countChangeTypes(changes);
          int totalChangeCount = changes.size();
          if (isRegularMove(changeTypeCount, totalChangeCount)) {
               moveType = MoveType.REGULAR;
          } else if (isTakeMove(changeTypeCount, totalChangeCount)) {
               moveType = MoveType.TAKE;
          } else if (isCastling(changeTypeCount, totalChangeCount)) {
               moveType = findCastlingSide(changes);
          } else if (isEnPassant(changeTypeCount, totalChangeCount)) {
               moveType = MoveType.EN_PASSANT;
          } else {
               moveType = MoveType.UNKNOWN;
          }
          return moveType;
     }

     private boolean isEnPassant(Map<ChangeType, Integer> changeTypeListMap, int totalChangeCount) {
          //todo add position validation
          return totalChangeCount == 3 &&
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
               int row = tileChange.getCoords().getColumn();
               kingSideCastling = (row == kingSideCastlingKingRow || row == kingSideCastlingRookRow);
          }

          return kingSideCastling ? MoveType.CASTLE_KING : MoveType.CASTLE_QUEEN;
     }

     private boolean isCastling(Map<ChangeType, Integer> changeTypeListMap, int totalChangeCount) {
          return totalChangeCount == 4 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 2 &&
                  changeTypeListMap.get(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE) == 2;
     }

     private boolean isRegularMove(Map<ChangeType, Integer> changeTypeListMap, int totalChangeCount) {
          return totalChangeCount ==  2 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 1 &&
                  changeTypeListMap.get(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE) == 1;
     }

     private boolean isTakeMove(Map<ChangeType, Integer> changeTypeListMap, int totalChangeCount) {
          return totalChangeCount == 2 &&
                  changeTypeListMap.get(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED) == 1 &&
                  changeTypeListMap.get(OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER) == 1;
     }

     public List<TileChange> findChangedTiles(Tile[][] tiles,
                                              Occupancy[][] newOccupancy,
                                              PlayerColor activeColor) {
          List<TileChange> changes = new LinkedList<>();
          for (int i = 0; i < BoardConsts.ROWS; ++i) {
               List<TileChange> rowChanges = analyzeRow(i, tiles[i], newOccupancy[i], activeColor);
               changes.addAll(rowChanges);
          }
          return changes;
     }

     private List<TileChange> analyzeRow(int row,
                                         Tile[] tilesRow,
                                         Occupancy[] newOccupancyRow,
                                         PlayerColor activeColor) {
          List<TileChange> changes = new LinkedList<>();
          for (int col = 0; col < BoardConsts.COLUMNS; ++col) {
               TileChange tileChange = analyzeTile(row, col, tilesRow[col], newOccupancyRow[col], activeColor);
               Optional.ofNullable(tileChange)
                       .ifPresent(changes::add);
          }
          return changes;
     }

     private TileChange analyzeTile(int row, int col, Tile tile, Occupancy newOccupancy, PlayerColor activeColor) {
          if (occupancyChanged(tile, newOccupancy)) {
               return createTileChange(row, col, tile, newOccupancy, activeColor);
          }
          return null;
     }

     private boolean occupancyChanged(Tile tile, Occupancy occupancy) {
          return !tile.getOccupancy().equals(occupancy);
     }

     private TileChange createTileChange(int row, int col, Tile tile, Occupancy newOccupancy, PlayerColor activeColor) {
          return TileChange.builder()
                           .coords(Coords.builder()
                                         .row(row)
                                         .column(col)
                                         .build())
                           .lastPiece(tile.getPiece())
                           .newOccupancy(newOccupancy)
                           .changeType(MoveAnalysisUtil.findChangeType(activeColor, newOccupancy, tile.getPiece()))
                           .build();
     }
}
