package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.analysis.service.AnalysisService;
import com.theeye.api.v1.chess.board.common.MoveAnalysisUtil;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.*;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.processor.MoveResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class MoveResolverService {

     private final AnalysisService analysisService;
     private final BoardService boardService;
     private final MoveResolver moveResolver;

     @Autowired
     public MoveResolverService(AnalysisService analysisService,
                                MoveResolver moveResolver,
                                BoardService boardService) {
          this.analysisService = analysisService;
          this.moveResolver = moveResolver;
          this.boardService = boardService;
     }

     public Board resolveMove(UnresolvedMove unresolvedMove) {

          Occupancy[][] tilesOccupancy = analysisService.getChessboardOccupancy(unresolvedMove);
          return determineNewState(unresolvedMove.getLastBoard(), tilesOccupancy);
     }

     public Board determineNewState(Board lastState, Occupancy[][] newOccupancy) {
          PlayerColor activeColor = lastState.getActiveColor();
          List<TileChange> changedTiles = findChangedTiles(lastState.getTiles(), newOccupancy, activeColor);
          MoveType moveType = moveResolver.findMoveType(changedTiles);
          if (moveType.equals(MoveType.UNKNOWN)) {
               throw new MoveDetectionException();
          }
          return boardService.doMove(lastState, changedTiles, moveType);
     }

     private List<TileChange> findChangedTiles(Tile[][] tiles,
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
          for (int col = 0; col < BoardConsts.COLUMNS; col++) {
               TileChange tileChange = analyzeTile(row, col, tilesRow[col], newOccupancyRow[col], activeColor);
               Optional.ofNullable(tileChange).ifPresent(changes::add);
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
