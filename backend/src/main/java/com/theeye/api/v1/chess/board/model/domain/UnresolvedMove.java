package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.analysis.model.dto.ReferenceColorsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnresolvedMove {

     Board lastBoard;
     ChessboardImage chessboardImage;
     ReferenceColorsDTO referenceColors;
}
