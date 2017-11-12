package com.theeye.api.v1.chess.fen.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor(staticName = "of")
public class Fen {

     private String fenDescription;
}
