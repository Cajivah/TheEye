package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.board.model.domain.ChessboardImage;
import com.theeye.api.v1.chess.board.model.dto.ChessboardImageDTO;
import com.theeye.common.Base64Util;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

     private static final String DEFAULT_IMAGE_FORMAT = "jpg";

     @Mapping(target = "image", source = "base64Image")
     public abstract ChessboardImage toChessboardImage(ChessboardImageDTO chessboardImageDTO);

     public BufferedImage toBufferedImage(String base64image) throws IOException {
          byte[] imageBytes = Base64Util.getImageBytes(base64image);
          return ImageIO.read(new ByteArrayInputStream(imageBytes));
     }

     public Mat toMat(BufferedImage bufferedImage) throws IOException {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(bufferedImage, DEFAULT_IMAGE_FORMAT, baos);
          byte[] bytes = baos.toByteArray();

          return Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
     }
}
