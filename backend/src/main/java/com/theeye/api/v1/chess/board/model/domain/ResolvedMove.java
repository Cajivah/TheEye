package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.board.model.enumeration.ResolvingStatus;
import lombok.Data;

import java.util.List;

@Data
public class ResolvedMove {

     private Board board;
     private List<ResolvingStatus> resolvingStatus;
}
