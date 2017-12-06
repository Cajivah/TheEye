package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.piece.model.domain.*;
import org.assertj.core.util.Lists;

import java.util.List;

import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.*;
import static com.theeye.api.v1.chess.board.model.enumeration.ChangeType.*;

public class ChangesTestFactory {

     public static List<TileChange> createChangesAfter1e4() {
          TileChange pawnUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(Pawn.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(6, 4))
                                                .build();

          TileChange pawnOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                              .coords(new Coords(4, 4))
                                              .build();

          return Lists.newArrayList(pawnUnoccupied, pawnOccupied);
     }

     public static List<TileChange> createChangesAfterTakeSetup1() {
          TileChange knightUnoccupied = TileChange.builder()
                                                  .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                  .lastPiece(Knight.of(WHITE))
                                                  .newOccupancy(Occupancy.UNOCCUPIED)
                                                  .coords(new Coords(5, 5))
                                                  .build();

          TileChange knightTakesPawn = TileChange.builder()
                                                 .changeType(OCCUPIED_BY_OPPONENT_TO_OCCUPIED_BY_ACTIVE_PLAYER)
                                                 .lastPiece(Pawn.of(BLACK))
                                                 .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                                 .coords(new Coords(3, 4))
                                                 .build();

          return Lists.newArrayList(knightUnoccupied, knightTakesPawn);
     }

     public static List<TileChange> createChangesAfterEnPassantSetup1() {
          TileChange pawnUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(Pawn.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(3, 0))
                                                .build();

          TileChange blackPawnUnoccupied = TileChange.builder()
                                                     .changeType(OCCUPIED_BY_OPPONENT_TO_UNOCCUPIED)
                                                     .lastPiece(Pawn.of(BLACK))
                                                     .newOccupancy(Occupancy.UNOCCUPIED)
                                                     .coords(new Coords(3, 1))
                                                     .build();

          TileChange pawnTaken = TileChange.builder()
                                           .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                           .lastPiece(Empty.of(NONE))
                                           .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                           .coords(new Coords(2, 1))
                                           .build();

          return Lists.newArrayList(pawnTaken, pawnUnoccupied, blackPawnUnoccupied);
     }

     public static List<TileChange> createChangesAfterEnPassantPossibleSetup1() {
          TileChange pawnUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(Pawn.of(BLACK))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(1, 1))
                                                .build();

          TileChange pawnOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_BLACK)
                                              .coords(new Coords(3, 1))
                                              .build();

          return Lists.newArrayList(pawnUnoccupied, pawnOccupied);
     }

     public static List<TileChange> createChangesAfterKingSideCastling() {
          TileChange rookUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(Rook.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(7, 7))
                                                .build();

          TileChange rookOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                              .coords(new Coords(7, 5))
                                              .build();

          TileChange kingUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(King.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(7, 4))
                                                .build();

          TileChange kingOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                              .coords(new Coords(7, 6))
                                              .build();

          return Lists.newArrayList(rookUnoccupied, rookOccupied, kingUnoccupied, kingOccupied);
     }

     public static List<TileChange> createChangesAfterQueenSideCastling() {
          TileChange rookUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(Rook.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(7, 0))
                                                .build();

          TileChange rookOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                              .coords(new Coords(7, 3))
                                              .build();

          TileChange queenUnoccupied = TileChange.builder()
                                                .changeType(OCCUPIED_BY_ACTIVE_TO_UNOCCUPIED)
                                                .lastPiece(King.of(WHITE))
                                                .newOccupancy(Occupancy.UNOCCUPIED)
                                                .coords(new Coords(7, 4))
                                                .build();

          TileChange queenOccupied = TileChange.builder()
                                              .changeType(UNOCCUPIED_TO_OCCUPIED_BY_ACTIVE)
                                              .lastPiece(Empty.of(NONE))
                                              .newOccupancy(Occupancy.OCCUPIED_BY_WHITE)
                                              .coords(new Coords(7, 2))
                                              .build();

          return Lists.newArrayList(rookUnoccupied, rookOccupied, queenUnoccupied, queenOccupied);
     }
}
