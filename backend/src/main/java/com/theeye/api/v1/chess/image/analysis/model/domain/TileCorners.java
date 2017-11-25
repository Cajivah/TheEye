package com.theeye.api.v1.chess.image.analysis.model.domain;

import lombok.Builder;
import lombok.Value;
import org.opencv.core.Point;

@Value
@Builder
public class TileCorners {

     private Point topRight;
     private Point bottomRight;
     private Point bottomLeft;
     private Point topLeft;
}
