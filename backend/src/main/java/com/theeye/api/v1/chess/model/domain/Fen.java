package com.theeye.api.v1.chess.model.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Fen {

   String fenDescription;
}
