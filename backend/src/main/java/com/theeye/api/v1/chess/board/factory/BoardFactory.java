package com.theeye.api.v1.chess.board.factory;


import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.Coords;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.piece.common.PieceInitialCoords;
import com.theeye.api.v1.chess.piece.common.PieceType;
import com.theeye.api.v1.chess.piece.model.domain.Piece;

import java.util.List;
import java.util.function.Supplier;

public class BoardFactory {

   public static Board createEmptyBoard() {
      return Board.builder()
                  .tiles(TileFactory.createInitialTiles())
                  .build();
   }

   public static Board createWithStartingPosition() {
      Board board = createEmptyBoard();
      for (PlayerColor player : PlayerColor.values()) {
         setUpPieces(board, player);
      }
      return board;
   }

   private static void setUpPieces(Board board, PlayerColor player) {
      final PieceType[] pieceTypes = PieceType.values();
      for (PieceType pieceType : pieceTypes) {
         placeAtInitialCoords(board, player, pieceType);
      }
   }

   private static void placeAtInitialCoords(Board board, PlayerColor player, PieceType pieceType) {
      final List<Coords> initialCoords = PieceInitialCoords.getForPieceAndPlayer(pieceType, player);
      final Supplier<Piece> pieceSupplier = pieceType.getPieceSupplier();
      for (Coords coords : initialCoords) {
         final Tile tile = prepareTile(player, pieceSupplier);
         board.setTileAt(coords, tile);
      }
   }

   private static Tile prepareTile(PlayerColor player, Supplier<Piece> pieceSupplier) {
      Piece piece = pieceSupplier.get();
      piece.setOwner(player);
      return TileFactory.createWithPiece(piece);
   }
}
