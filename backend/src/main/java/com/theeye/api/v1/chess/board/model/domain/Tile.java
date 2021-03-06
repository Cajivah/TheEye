package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.piece.model.domain.Empty;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.NONE;
import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.WHITE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Tile {

     @NotNull
     private Piece piece;

     public boolean isOccupied() {
          return !piece.getOwner().equals(NONE);
     }

     public Occupancy getOccupancy() {
          if (!isOccupied()) {
               return Occupancy.UNOCCUPIED;
          } else if (piece.getOwner().equals(WHITE)) {
               return Occupancy.OCCUPIED_BY_WHITE;
          } else {
               return Occupancy.OCCUPIED_BY_BLACK;
          }
     }

     public void unoccupy() {
          piece = Empty.of(NONE);
     }

     public char getFen() {
          return piece.getFenCode();
     }
}
