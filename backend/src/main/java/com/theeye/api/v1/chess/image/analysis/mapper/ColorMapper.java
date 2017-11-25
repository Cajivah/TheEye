package com.theeye.api.v1.chess.image.analysis.mapper;

import com.theeye.api.v1.chess.image.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.domain.TileReferenceColors;
import com.theeye.api.v1.chess.image.analysis.model.dto.RGBColorDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.ReferenceColorsDTO;
import com.theeye.api.v1.chess.image.analysis.model.dto.TileReferenceColorsDTO;
import org.mapstruct.Mapper;
import org.opencv.core.Scalar;

import static com.theeye.api.v1.chess.image.analysis.util.ColorUtils.*;

@Mapper(componentModel = "spring")
public abstract class ColorMapper {

     public ReferenceColorsDTO toReferenceColorsDTO(ReferenceColors referenceColors) {
          return ReferenceColorsDTO.builder()
                                   .whiteTiles(toTileReferenceColorsDTO(referenceColors.getWhiteTiles()))
                                   .blackTiles(toTileReferenceColorsDTO(referenceColors.getBlackTiles()))
                                   .build();
     }

     public TileReferenceColorsDTO toTileReferenceColorsDTO(TileReferenceColors tileReferenceColors) {
          return TileReferenceColorsDTO.builder()
                                       .occupiedByWhite(toRGBColorDTO(tileReferenceColors.getOccupiedByWhite()))
                                       .occupiedByBlack(toRGBColorDTO(tileReferenceColors.getOccupiedByBlack()))
                                       .unoccupied(toRGBColorDTO(tileReferenceColors.getUnoccupied()))
                                       .build();
     }

     public RGBColorDTO toRGBColorDTO(Scalar scalar) {
          return RGBColorDTO.builder()
                            .red((int) scalar.val[SCALAR_RED_INDEX])
                            .green((int) scalar.val[SCALAR_GREEN_INDEX])
                            .blue((int) scalar.val[SCALAR_BLUE_INDEX])
                            .build();
     }

     public ReferenceColors toReferenceColors(ReferenceColorsDTO referenceColorsDTO) {
          return ReferenceColors.builder()
                                .whiteTiles(toTileReferenceColors(referenceColorsDTO.getWhiteTiles()))
                                .blackTiles(toTileReferenceColors(referenceColorsDTO.getBlackTiles()))
                                .build();
     }

     private TileReferenceColors toTileReferenceColors(TileReferenceColorsDTO tileReferenceColorsDTO) {
          return TileReferenceColors.builder()
                                    .occupiedByWhite(toScalar(tileReferenceColorsDTO.getOccupiedByWhite()))
                                    .occupiedByBlack(toScalar(tileReferenceColorsDTO.getOccupiedByBlack()))
                                    .unoccupied(toScalar(tileReferenceColorsDTO.getUnoccupied()))
                                    .build();
     }

     private Scalar toScalar(RGBColorDTO rgbColorDTO) {
          return new Scalar(rgbColorDTO.getBlue(), rgbColorDTO.getGreen(), rgbColorDTO.getRed());
     }
}
