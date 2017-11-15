package com.theeye.api.v1.chess.fen.parser;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.CastlingStatus;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.fen.common.FenCodes;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.springframework.stereotype.Component;

@Component
public class FenEncoder {

     public Fen encode(Board board) {
          String fenDescription =
                  encodePositions(board.getTiles()) +
                          FenCodes.SECTION_DELIMITER +
                          encodeActiveColour(board.getActiveColour()) +
                          FenCodes.SECTION_DELIMITER +
                          encodeCastling(board.getCastling()) +
                          FenCodes.SECTION_DELIMITER +
                          encodeEnPassant(board.getEnPassant()) +
                          FenCodes.SECTION_DELIMITER +
                          encodeHalfmoveClock(board.getHalfmoveClock()) +
                          FenCodes.SECTION_DELIMITER +
                          encodeFullmoveNumber(board.getFullmoveNumber());

          return Fen.of(fenDescription);
     }

     private String encodePositions(Tile[][] tiles) {
          StringBuilder positions = new StringBuilder();
          int initialIndex = tiles.length - 1;
          for (int i = initialIndex; i >= 0; --i) {
               positions.append(i == initialIndex ? "" : FenCodes.ROW_DELIMITER)
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
               if (fenCode == FenCodes.NO_PIECE) {
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
          if (activeColour.equals(PlayerColor.WHITE)) {
               return FenCodes.WHITE_ACTIVE;
          } else if (activeColour.equals(PlayerColor.BLACK)) {
               return FenCodes.BLACK_ACTIVE;
          } else {
               return FenCodes.EMPTY;
          }
     }

     private String encodeCastling(CastlingStatus castling) {
          StringBuilder castlingDescription = new StringBuilder();
          if (castling.isKingSideWhiteValid()) {
               castlingDescription.append(FenCodes.KING_WHITE);
          }
          if (castling.isQueenSideWhiteValid()) {
               castlingDescription.append(FenCodes.QUEEN_WHITE);
          }
          if (castling.isKingSideBlackValid()) {
               castlingDescription.append(FenCodes.KING_BLACK);
          }
          if (castling.isQueenSideBlackValid()) {
               castlingDescription.append(FenCodes.QUEEN_BLACK);
          }
          return castlingDescription.length() == 0
                  ? FenCodes.EMPTY
                  : castlingDescription.toString();
     }

     private String encodeEnPassant(String enPassant) {
          return enPassant;
     }

     private String encodeHalfmoveClock(Integer halfmoveClock) {
          return halfmoveClock == null
                  ? FenCodes.EMPTY
                  : String.valueOf(halfmoveClock);
     }

     private String encodeFullmoveNumber(Integer fullmoveNumber) {
          return fullmoveNumber == null
                  ? FenCodes.EMPTY
                  : String.valueOf(fullmoveNumber);
     }
}
