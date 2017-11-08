package com.theeye.api.v1.chess.board.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class Game {

     private Board board;

     private List<Move> movesHistory;
}
