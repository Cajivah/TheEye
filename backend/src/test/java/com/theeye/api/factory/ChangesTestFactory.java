package com.theeye.api.factory;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.piece.model.domain.Empty;
import com.theeye.api.v1.chess.piece.model.domain.Knight;
import com.theeye.api.v1.chess.piece.model.domain.Pawn;
import org.assertj.core.util.Lists;

import java.util.List;

import static com.theeye.api.v1.chess.board.common.PlayerColor.BLACK;
import static com.theeye.api.v1.chess.board.common.PlayerColor.NONE;
import static com.theeye.api.v1.chess.board.common.PlayerColor.WHITE;
import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED;
import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER;
import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE;

public class ChangesTestFactory {

     public static List<TileChange> createChangesAfter1e4() {
          TileChange pawnUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(Pawn.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(1, 4))
                                                .build();

          TileChange pawnOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                              .coords(new Coords(3, 4))
                                              .build();

          return Lists.newArrayList(pawnUnoccupied, pawnOccupied);
     }

     public static List<TileChange> createChangesAfterTakeSetup1() {
          TileChange knightUnoccupied = TileChange.builder()
                                                  .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                  .lastPiece(Knight.of(WHITE))
                                                  .newOccupancy(Occupancy.UNOCCUPIED)
                                                  .coords(new Coords(2, 5))
                                                  .build();

          TileChange knightTakesPawn = TileChange.builder()
                                                 .changeType(OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER)
                                                 .lastPiece(Pawn.of(BLACK))
                                                 .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                                 .coords(new Coords(4, 4))
                                                 .build();

          return Lists.newArrayList(knightUnoccupied, knightTakesPawn);
     }
}
