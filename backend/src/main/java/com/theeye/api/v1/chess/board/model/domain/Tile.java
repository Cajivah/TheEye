package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.fen.common.FenCodes;
import com.theeye.api.v1.chess.piece.model.domain.Empty;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.theeye.api.v1.chess.board.common.PlayerColor.*;

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
