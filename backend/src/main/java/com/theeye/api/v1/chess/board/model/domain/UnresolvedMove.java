package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.image.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileCorners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnresolvedMove {

     Board lastBoard;
     ChessboardImage chessboardImage;
     ReferenceColors referenceColors;
     Point[] chessboardCorners;
     TileCorners[][] tilesCorners;
}
