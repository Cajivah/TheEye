package com.theeye.api.v1.common.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDTO {

     private String message;
     private String code;
}
