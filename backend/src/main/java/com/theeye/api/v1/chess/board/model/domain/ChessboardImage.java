package com.theeye.api.v1.chess.board.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChessboardImage {

     private BufferedImage image;
     private boolean warped = false;
     private boolean greyscaled = false;
     private boolean rotated = false;
}
