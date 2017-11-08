package com.theeye.api.v1.chess.piece.common.fen;

import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.model.domain.CastlingStatus;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.piece.common.PieceType;
import com.theeye.api.v1.chess.piece.model.domain.Empty;
import com.theeye.com.theeye.common.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.theeye.api.v1.chess.piece.common.fen.FenCodes.*;

@Component
public class FenParser {

     public PlayerColor parseActiveColour(String fenSection) {
          String sanitized = fenSection.trim();
          switch (sanitized) {
               case WHITE_ACTIVE:
                    return PlayerColor.White;
               case BLACK_ACTIVE:
                    return PlayerColor.Black;
               default:
                    return PlayerColor.None;
          }
     }

     public CastlingStatus parseCastling(String fenSection) {
          String sanitized = fenSection.trim();
          return CastlingStatus.builder()
                               .kingSideWhiteValid(sanitized.contains(String.valueOf(KING_WHITE)))
                               .queenSideWhiteValid(sanitized.contains(String.valueOf(QUEEN_WHITE)))
                               .kingSideBlackValid(sanitized.contains(String.valueOf(KING_BLACK)))
                               .queenSideBlackValid(sanitized.contains(String.valueOf(QUEEN_BLACK)))
                               .build();
     }

     public String parseEnPassant(String fenSection) {
          return fenSection.trim();
     }

     public Integer parseHalfmoveClock(String fenSection) {
          String sanitized = fenSection.trim();
          return StringUtil.isNullOrEmpty(sanitized)
                  ? null
                  : Integer.parseInt(sanitized);
     }

     public Integer parseFullmoveNumber(String fenSection) {
          String sanitized = fenSection.trim();
          return StringUtil.isNullOrEmpty(sanitized)
                  ? null
                  : Integer.parseInt(sanitized);
     }

     public Tile[][] parsePositions(String fenSection) {
          String[] split = fenSection.split(String.valueOf(ROW_DELIMITER));
          ArrayUtils.reverse(split);
          return Arrays.stream(split)
                       .map(String::toCharArray)
                       .map(this::parseFenRow)
                       .toArray(size -> new Tile[BoardConsts.ROWS][BoardConsts.COLUMNS]);
     }

     private Tile[] parseFenRow(char[] row) {
          List<Tile> tiles = new LinkedList<>();
          for (char code : row) {
               parseFenCode(tiles, code);
          }
          return tiles.toArray(new Tile[BoardConsts.COLUMNS]);
     }

     private void parseFenCode(List<Tile> tiles, char code) {
          if (Character.isDigit(code)) {
               parseFenNumber(tiles, code);
          } else {
               parseFenChar(tiles, code);
          }
     }

     private void parseFenChar(List<Tile> tiles, char code) {
          PieceType pieceType = FenCodeToPieceTypeMap.forFen(code);
          tiles.add(Tile.of(pieceType.getPieceSupplier().get()));
     }

     private void parseFenNumber(List<Tile> tiles, char code) {
          for (int i = Character.getNumericValue(code) - 1; i >= 0; --i) {
               tiles.add(Tile.of(Empty.of(PlayerColor.None)));
          }
     }
}
