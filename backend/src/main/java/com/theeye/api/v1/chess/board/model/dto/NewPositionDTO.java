package com.theeye.api.v1.chess.board.model.dto;

import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPositionDTO {

     private Fen fen;
     private List<MoveType> moveTypes;
}
