package com.theeye.api.v1.chess.fen.parser;

import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.PlayersCastlingStatuses;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.fen.model.consts.FenCodes;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.springframework.stereotype.Component;

@Component
public class FenEncoder {

     public Fen encode(Board board) {
          String fenDescription =
                  encodePositions(board.getTiles()) +
                          FenCodes.SECTION_DELIMITER +
                          encodeActiveColor(board.getActiveColor()) +
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
          int rowCount = tiles.length;
          for (int i = 0; i < rowCount; ++i) {
               positions.append(i == 0 ? "" : FenCodes.ROW_DELIMITER)
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

     private String encodeActiveColor(PlayerColor activeColor) {
          if (activeColor.equals(PlayerColor.WHITE)) {
               return FenCodes.WHITE_ACTIVE;
          } else if (activeColor.equals(PlayerColor.BLACK)) {
               return FenCodes.BLACK_ACTIVE;
          } else {
               return FenCodes.EMPTY;
          }
     }

     private String encodeCastling(PlayersCastlingStatuses castling) {
          StringBuilder castlingDescription = new StringBuilder();
          if (castling.getWhite().isKingSideCastle()) {
               castlingDescription.append(FenCodes.KING_WHITE);
          }
          if (castling.getWhite().isQueenSideCastle()) {
               castlingDescription.append(FenCodes.QUEEN_WHITE);
          }
          if (castling.getBlack().isKingSideCastle()) {
               castlingDescription.append(FenCodes.KING_BLACK);
          }
          if (castling.getBlack().isQueenSideCastle()) {
               castlingDescription.append(FenCodes.QUEEN_BLACK);
          }
          return castlingDescription.length() == 0
                  ? FenCodes.EMPTY
                  : castlingDescription.toString();
     }

     private String encodeEnPassant(String enPassant) {
          return enPassant == null
                  ? FenCodes.EMPTY
                  : enPassant;
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
