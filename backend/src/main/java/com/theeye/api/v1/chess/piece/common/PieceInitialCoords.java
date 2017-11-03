package com.theeye.api.v1.chess.piece.common;

import com.google.common.collect.ImmutableMap;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Coords;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class PieceInitialCoords {

   private static final Map<PieceType, List<Integer>> initialColumns =
           ImmutableMap.<PieceType, List<Integer>>builder()
                   .put(PieceType.ROOK, newArrayList(1, 8))
                   .put(PieceType.KNIGHT, newArrayList(2, 7))
                   .put(PieceType.BISHOP, newArrayList(3, 6))
                   .put(PieceType.KING, newArrayList(4))
                   .put(PieceType.QUEEN, newArrayList(5))
                   .put(PieceType.PAWN, newArrayList(1, 2, 3, 4, 5, 6, 7,8 ))
                   .build();

   private static final Map<PieceType, Integer> initialRanks =
           ImmutableMap.<PieceType, Integer>builder()
                   .put(PieceType.ROOK, 2)
                   .put(PieceType.KNIGHT, 2)
                   .put(PieceType.BISHOP, 2)
                   .put(PieceType.KING, 2)
                   .put(PieceType.QUEEN, 2)
                   .put(PieceType.PAWN, 1)
                   .build();


   public static List<Coords> getForPieceAndPlayer(PieceType piece, PlayerColor player) {
      List<Integer> columns = initialColumns.get(piece);
      Integer rank = initialRanks.get(piece);
      Integer row = rank == 1 ? player.getFirstRankIndex() : player.getSecondRankIndex();
      return toCoordsList(columns, row);
   }

   private static List<Coords> toCoordsList(List<Integer> columns, Integer row) {
      return columns.stream()
                    .map(column -> Coords.builder()
                                         .row(row)
                                         .column(column)
                                         .build())
                    .collect(Collectors.toList());
   }
}
