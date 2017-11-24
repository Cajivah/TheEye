package com.theeye.api.factory;

import com.theeye.api.v1.chess.board.model.consts.BoardConsts;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;

import java.util.stream.IntStream;

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

     public static Occupancy[][] createOccupancyBeforeTakeSetup1() {
          return new Occupancy[][]{
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE,
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_WHITE,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_BLACK,
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  }
          };
     }

     public static Occupancy[][] createOccupancyAfterTakeSetup1() {
          return new Occupancy[][]{
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE,
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_WHITE,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, OCCUPIED_BY_BLACK, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_BLACK,
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  }
          };
     }
     public static Occupancy[][] createOccupancyBeforeEnPassantPossibleSetup1() {
          return new Occupancy[][]{
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, UNOCCUPIED, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK,
                          UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  }
          };
     }

     public static Occupancy[][] createOccupancyAfterEnPassantPossibleSetup1() {
          return new Occupancy[][]{
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_BLACK,
                          UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  }
          };
     }

     public static Occupancy[][] createOccupancyAfterEnPassantSetup1() {
          return new Occupancy[][]{
                  new Occupancy[] {
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, UNOCCUPIED,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_WHITE, OCCUPIED_BY_WHITE,
                          UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_BLACK, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED
                  },
                  new Occupancy[] {
                          UNOCCUPIED, OCCUPIED_BY_WHITE, UNOCCUPIED, UNOCCUPIED,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, UNOCCUPIED, UNOCCUPIED
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, UNOCCUPIED, UNOCCUPIED, OCCUPIED_BY_BLACK,
                          UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  },
                  new Occupancy[] {
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK,
                          OCCUPIED_BY_BLACK, UNOCCUPIED, OCCUPIED_BY_BLACK, OCCUPIED_BY_BLACK
                  }
          };
     }
}
