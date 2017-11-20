package com.theeye.api.factory;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.model.consts.BoardConsts;

import java.util.stream.IntStream;

import static com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy.*;

public class OccupancyTestFactory {

     public static Occupancy[][] createOccupancyAfter1e4() {
          return new Occupancy[][]{
                  createRowOccupiedBy(OCCUPIED_BY_WHITE),
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  createRowOccupiedBy(UNOCCUPIED),
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  createRowOccupiedBy(UNOCCUPIED),
                  createRowOccupiedBy(UNOCCUPIED),
                  createRowOccupiedBy(OCCUPIED_BY_BLACK),
                  createRowOccupiedBy(OCCUPIED_BY_BLACK)
          };
     }

     private static Occupancy[] createRowOccupiedBy(Occupancy occupancy) {
          return IntStream.range(0, BoardConsts.COLUMNS)
                          .boxed()
                          .map(i -> occupancy)
                          .toArray(Occupancy[]::new);
     }
}
