package com.theeye.api.v1.chess.fen.model.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Fen {

     private String fenDescription;

     public static Fen of(String fen) {
          return new Fen(fen);
     }
}
