package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import lombok.Data;

import java.util.List;

@Data
public class ResolvedMove {

     private Board board;
     private List<MoveType> moveTypes;
}
