package com.theeye.api.v1.chess.board.model.dto;

import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class ChessboardImageDTO {

     @NotNull
     @NotEmpty
     private String base64Image;
     private boolean warped;
     private boolean greyscaled;
     private boolean rotated;
}
