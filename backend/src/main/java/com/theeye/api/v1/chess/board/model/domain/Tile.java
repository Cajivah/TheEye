package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.piece.common.fen.FenCodes;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Tile {

     @Nullable
     private Piece piece;

     public boolean isOccupied() {
          return Optional.ofNullable(piece)
                         .isPresent();
     }

     public void unoccupy() {
          piece = null;
     }

     public char getFen() {
          return Optional.ofNullable(piece)
                         .map(Piece::getFenCode)
                         .orElse(FenCodes.NO_PIECE);
     }
}
