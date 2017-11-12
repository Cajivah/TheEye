package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.board.model.domain.ChessboardImage;
import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import com.theeye.common.Base64Util;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

     @Mapping(target = "image", source = "base64Image")
     public abstract ChessboardImage toChessboardImage(ChessboardImageDTO chessboardImageDTO);

     public BufferedImage toBufferedImage(String base64image) throws IOException {
          byte[] imageBytes = Base64Util.getImageBytes(base64image);
          return ImageIO.read(new ByteArrayInputStream(imageBytes));
     }

     public Mat toMat(BufferedImage bufferedImage) {
          Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
          byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
          mat.put(0, 0, data);
          return mat;
     }
}
