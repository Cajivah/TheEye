package com.theeye.api.v1.chess.board.service;

import com.theeye.api.factory.ChangesTestFactory;
import com.theeye.api.factory.OccupancyTestFactory;
import com.theeye.api.factory.TileTestFactory;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveTypeServiceTest {

     MoveTypeService sut = new MoveTypeService();

     @Nested
     @DisplayName("Given tiles")
     class GivenTiles {

          Tile[][] tiles;
          Occupancy[][] occupancies;

          @Nested
          @DisplayName("Finding changes")
          class FindChanges {

               @Test
               @DisplayName("Should return list of changes after regular move")
               void findChangedTilesAfter1e4() {
                    tiles = TileTestFactory.createInitial();
                    occupancies = OccupancyTestFactory.createOccupancyAfter1e4();

                    List<TileChange> changedTiles = sut.findChangedTiles(tiles, occupancies, PlayerColor.WHITE);
                    List<TileChange> expectedChanges = ChangesTestFactory.createChangesAfter1e4();
                    assertAll("Assert that correct changes found",
                            () -> assertEquals(2, changedTiles.size()),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(0))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(1)))
                    );
               }

               @Test
               @DisplayName("Should return list of changes after taking move")
               void findChangesAfterTake1() {
                    tiles = TileTestFactory.createForBeforeTakeSetup1();
                    occupancies = OccupancyTestFactory.createOccupancyAfterTakeSetup1();

                    List<TileChange> changedTiles = sut.findChangedTiles(tiles, occupancies, PlayerColor.WHITE);
                    List<TileChange> expectedChanges = ChangesTestFactory.createChangesAfterTakeSetup1();
                    assertAll("Assert that correct changes found",
                            () -> assertEquals(2, changedTiles.size()),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(0))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(1)))
                    );
               }

               @Test
               @DisplayName("Should return list of changes after en passant move")
               void findChangesAfterEnPassantSetup1() {
                    tiles = TileTestFactory.createForAfterEnPassantPossibleSetup1();
                    occupancies = OccupancyTestFactory.createOccupancyAfterEnPassantSetup1();

                    List<TileChange> changedTiles = sut.findChangedTiles(tiles, occupancies, PlayerColor.WHITE);
                    List<TileChange> expectedChanges = ChangesTestFactory.createChangesAfterEnPassantSetup1();
                    assertAll("Assert that correct changes found",
                            () -> assertEquals(3, changedTiles.size()),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(0))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(1))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(2)))
                    );
               }

               @Test
               @DisplayName("Should return list of changes after king side castling move")
               void findChangesAfterKingSideCastling() {
                    tiles = TileTestFactory.createBeforeKingSideCastle();
                    occupancies = OccupancyTestFactory.createAfterKingSideCastling();

                    List<TileChange> changedTiles = sut.findChangedTiles(tiles, occupancies, PlayerColor.WHITE);
                    List<TileChange> expectedChanges = ChangesTestFactory.createChangesAfterKingSideCastling();
                    assertAll("Assert that correct changes found",
                            () -> assertEquals(4, changedTiles.size()),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(0))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(1))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(2))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(3)))
                    );
               }

               @Test
               @DisplayName("Should return list of changes after queen side castling move")
               void findChangesAfterQueenSideCastle() {
                    tiles = TileTestFactory.createBeforeQueenSideCastle();
                    occupancies = OccupancyTestFactory.createAfterQueenSideCastling();

                    List<TileChange> changedTiles = sut.findChangedTiles(tiles, occupancies, PlayerColor.WHITE);
                    List<TileChange> expectedChanges = ChangesTestFactory.createChangesAfterQueenSideCastling();
                    assertAll("Assert that correct changes found",
                            () -> assertEquals(4, changedTiles.size()),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(0))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(1))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(2))),
                            () -> assertTrue(changedTiles.contains(expectedChanges.get(3)))
                    );
               }
          }
     }

     @Nested
     @DisplayName("Given list of changes")
     class GivenChanges {

          List<TileChange> tileChanges;

          @Nested
          @DisplayName("Finding move type")
          class FindChanges {

               @Test
               @DisplayName("Should classify regular move")
               void findRegularMoveType() {
                    tileChanges = ChangesTestFactory.createChangesAfter1e4();
                    MoveType moveType = sut.findMoveType(tileChanges);
                    assertEquals(MoveType.REGULAR, moveType);
               }

               @Test
               @DisplayName("Should classify taking move")
               void findTakingMoveType() {
                    tileChanges = ChangesTestFactory.createChangesAfterTakeSetup1();
                    MoveType moveType = sut.findMoveType(tileChanges);
                    assertEquals(MoveType.TAKE, moveType);
               }

               @Test
               @DisplayName("Should classify en passant move")
               void findEnPassantMoveType() {
                    tileChanges = ChangesTestFactory.createChangesAfterEnPassantSetup1();
                    MoveType moveType = sut.findMoveType(tileChanges);
                    assertEquals(MoveType.EN_PASSANT, moveType);
               }

               @Test
               @DisplayName("Should classify en king side castle move")
               void findKingSideCastleMoveType() {
                    tileChanges = ChangesTestFactory.createChangesAfterKingSideCastling();
                    MoveType moveType = sut.findMoveType(tileChanges);
                    assertEquals(MoveType.CASTLE_KING, moveType);
               }

               @Test
               @DisplayName("Should classify queen side castle move move")
               void findQueenSideCastleMoveType() {
                    tileChanges = ChangesTestFactory.createChangesAfterQueenSideCastling();
                    MoveType moveType = sut.findMoveType(tileChanges);
                    assertEquals(MoveType.CASTLE_QUEEN, moveType);
               }
          }
     }
}