package com.theeye.api.v1.chess.image.analysis.exception;

public class PatternNotFoundException extends RuntimeException {

     public static final String DEFAULT_MESSAGE = "Patter was not found in given image. Could not further process";

     public PatternNotFoundException() {
          super(DEFAULT_MESSAGE);
     }
}
