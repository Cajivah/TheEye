package com.theeye.api.v1.chess.board.model.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CastlingStatus {

     private boolean queenSideWhiteValid = true;
     private boolean kingSideWhiteValid = true;
     private boolean queenSideBlackValid = true;
     private boolean kingSideBlackValid = true;
}
