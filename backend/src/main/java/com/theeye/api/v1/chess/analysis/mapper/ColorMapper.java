package com.theeye.api.v1.chess.analysis.mapper;

import com.theeye.api.v1.chess.analysis.model.domain.ReferenceColors;
import com.theeye.api.v1.chess.analysis.model.domain.TileReferenceColors;
import com.theeye.api.v1.chess.analysis.model.dto.RGBColorDTO;
import com.theeye.api.v1.chess.analysis.model.dto.ReferenceColorsDTO;
import com.theeye.api.v1.chess.analysis.model.dto.TileReferenceColorsDTO;
import org.mapstruct.Mapper;
import org.opencv.core.Scalar;

import static com.theeye.api.v1.chess.analysis.util.ColorUtils.*;

@Mapper(componentModel = "spring")
public abstract class ColorMapper {

     public ReferenceColorsDTO toReferenceColorsDTO(ReferenceColors referenceColors) {
          return ReferenceColorsDTO.builder()
                                   .whiteTile(toTileReferenceColorsDTO(referenceColors.getWhiteTiles()))
                                   .blackTile(toTileReferenceColorsDTO(referenceColors.getBlackTiles()))
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
}
