package com.theeye.api.v1.chess.piece.common.fen;

import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.model.domain.Fen;
import com.theeye.api.v1.chess.piece.common.PieceType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class FenParser {
     public Board decode(Fen fen) {
          String[] split = fen.getFenDescription().split(String.valueOf(FenCodes.ROW_DELIMITER));
          Tile[][] tiles = Arrays.stream(split)
                                 .map(String::toCharArray)
                                 .map(this::parseFenRow)
                                 .toArray(size -> new Tile[BoardConsts.ROWS][BoardConsts.COLUMNS]);
          return Board.builder()
                      .tiles(tiles)
                      .build();
     }

     private Tile[] parseFenRow(char[] row) {
          List<Tile> tiles = new LinkedList<>();
          for (char code : row) {
               parseFenCode(tiles, code);
          }
          return tiles.toArray(new Tile[BoardConsts.COLUMNS]);
     }

     private void parseFenCode(List<Tile> tiles, char code) {
          if(Character.isDigit(code)) {
               parseFenNumber(tiles, code);
          } else {
               parseFenChar(tiles, code);
          }
     }

     private void parseFenChar(List<Tile> tiles, char code) {
          PieceType pieceType = FenCodeToPieceTypeMap.forFen(code);
          tiles.add(new Tile(pieceType.getPieceSupplier().get()));
     }

     private void parseFenNumber(List<Tile> tiles, char code) {
          for (int i = Character.getNumericValue(code); i >= 0; --i) {
               tiles.add(new Tile());
          }
     }

     public Fen encode(Board board) {
          Tile[][] tiles = board.getTiles();
          StringBuilder fenBuilder = new StringBuilder();
          for (Tile[] tile : tiles) {
               fenBuilder.append(encodeRow(tile))
                         .append(FenCodes.ROW_DELIMITER);
          }
          return Fen.builder()
                    .fenDescription(fenBuilder.toString())
                    .build();
     }

     private String encodeRow(Tile[] tilesRow) {
          StringBuilder fenBuilder = new StringBuilder();
          for (Tile tile : tilesRow) {
               fenBuilder.append(tile.getFen());
          }
          return fenBuilder.toString();
     }
}
