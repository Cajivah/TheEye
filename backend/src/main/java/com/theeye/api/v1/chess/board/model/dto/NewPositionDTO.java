package com.theeye.api.v1.chess.board.model.dto;

import com.theeye.api.v1.chess.board.model.enumeration.ResolvingStatus;
import com.theeye.api.v1.chess.model.domain.Fen;
import lombok.Data;

import java.util.List;

@Data
public class NewPositionDTO {

     private Fen position;
     private List<ResolvingStatus> resolvingStatus;
}
