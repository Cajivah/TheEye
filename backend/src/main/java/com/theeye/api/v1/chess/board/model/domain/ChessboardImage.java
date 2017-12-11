package com.theeye.api.v1.chess.board.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.core.Mat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChessboardImage {

     private Mat image;
     private boolean warped = false;
     private boolean rotated = false;
     private boolean cropped = false;
}
