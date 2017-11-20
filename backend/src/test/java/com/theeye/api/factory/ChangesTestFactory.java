package com.theeye.api.factory;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.piece.model.domain.Empty;
import com.theeye.api.v1.chess.piece.model.domain.Pawn;
import org.assertj.core.util.Lists;

import java.util.List;

import static com.theeye.api.v1.chess.board.common.PlayerColor.*;
import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED;
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
}
