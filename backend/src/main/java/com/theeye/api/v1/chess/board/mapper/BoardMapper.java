package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.board.common.BoardConsts;
import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.board.model.domain.ResolvingResult;
import com.theeye.api.v1.chess.board.model.domain.Tile;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import com.theeye.api.v1.chess.model.domain.Fen;
import com.theeye.api.v1.chess.piece.common.PieceType;
import com.theeye.api.v1.chess.piece.common.fen.FenCodeToPieceTypeMap;
import com.theeye.api.v1.chess.piece.common.fen.FenCodes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BoardMapper {

   public Board toBoard(Fen fen) {
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

   public Fen toFEN(Board board) {
      return null;
   }

   @Mapping(target = "position", source = "board")
   public abstract NewPositionDTO toNewPositionDTO(ResolvingResult resolvingResult);
}
