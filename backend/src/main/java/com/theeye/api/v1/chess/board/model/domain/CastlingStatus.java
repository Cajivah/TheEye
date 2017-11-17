package com.theeye.api.v1.chess.board.model.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CastlingStatus {

     @Builder.Default
     private boolean queenSideCastle = true;

     @Builder.Default
     private boolean kingSideCastle = true;

     public boolean canCastle() {
          return kingSideCastle || queenSideCastle;
     }
}
