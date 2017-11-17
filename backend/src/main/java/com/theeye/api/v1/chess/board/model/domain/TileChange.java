package com.theeye.api.v1.chess.board.model.domain;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.model.enumeration.ChangeType;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TileChange {

     private Coords coords;
     private Piece lastPiece;
     private Occupancy newOccupancy;
     private ChangeType changeType;
}
