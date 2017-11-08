package com.theeye.api.v1.chess.piece.common.fen;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.CastlingStatus;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.model.domain.Fen;
import org.springframework.stereotype.Component;

import static com.theeye.api.v1.chess.piece.common.fen.FenCodes.*;

@Component
public class FenEncoder {

     public Fen encode(Board board) {
          String fenDescription =
                  encodePositions(board.getTiles()) +
                          SECTION_DELIMITER +
                          encodeActiveColour(board.getActiveColour()) +
                          SECTION_DELIMITER +
                          encodeCastling(board.getCastling()) +
                          SECTION_DELIMITER +
                          encodeEnPassant(board.getEnPassant()) +
                          SECTION_DELIMITER +
                          encodeHalfmoveClock(board.getHalfmoveClock()) +
                          SECTION_DELIMITER +
                          encodeFullmoveNumber(board.getFullmoveNumber());

          return Fen.of(fenDescription);
     }

     private String encodePositions(Tile[][] tiles) {
          StringBuilder positions = new StringBuilder();
          int initialIndex = tiles.length - 1;
          for (int i = initialIndex; i >= 0; --i) {
               positions.append(i == initialIndex ? "" : ROW_DELIMITER)
                        .append(encodeRow(tiles[i]));
          }
          return positions.toString();
     }

     private String encodeRow(Tile[] tilesRow) {
          StringBuilder fenBuilder = new StringBuilder();
          int aggregatedEmptyTiles = 0;
          int i = 0;
          while (i < tilesRow.length) {
               char fenCode = tilesRow[i++].getFen();
               if (fenCode == NO_PIECE) {
                    ++aggregatedEmptyTiles;
               } else {
                    if (aggregatedEmptyTiles > 0) {
                         fenBuilder.append(aggregatedEmptyTiles);
                         aggregatedEmptyTiles = 0;
                    }
                    fenBuilder.append(fenCode);
               }
          }
          if (aggregatedEmptyTiles > 0) {
               fenBuilder.append(aggregatedEmptyTiles);
          }
          return fenBuilder.toString();
     }

     private String encodeActiveColour(PlayerColor activeColour) {
          if (activeColour.equals(PlayerColor.White)) {
               return WHITE_ACTIVE;
          } else if (activeColour.equals(PlayerColor.Black)) {
               return BLACK_ACTIVE;
          } else {
               return EMPTY;
          }
     }

     private String encodeCastling(CastlingStatus castling) {
          StringBuilder castlingDescription = new StringBuilder();
          if (castling.isKingSideWhiteValid()) {
               castlingDescription.append(KING_WHITE);
          }
          if (castling.isQueenSideWhiteValid()) {
               castlingDescription.append(QUEEN_WHITE);
          }
          if (castling.isKingSideBlackValid()) {
               castlingDescription.append(KING_BLACK);
          }
          if (castling.isQueenSideBlackValid()) {
               castlingDescription.append(QUEEN_BLACK);
          }
          return castlingDescription.length() == 0
                  ? EMPTY
                  : castlingDescription.toString();
     }

     private String encodeEnPassant(String enPassant) {
          return enPassant;
     }

     private String encodeHalfmoveClock(Integer halfmoveClock) {
          return halfmoveClock == null
                  ? EMPTY
                  : String.valueOf(halfmoveClock);
     }

     private String encodeFullmoveNumber(Integer fullmoveNumber) {
          return fullmoveNumber == null
                  ? EMPTY
                  : String.valueOf(fullmoveNumber);
     }
}
