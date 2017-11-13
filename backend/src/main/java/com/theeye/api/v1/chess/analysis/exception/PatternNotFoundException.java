package com.theeye.api.v1.chess.analysis.exception;

import java.util.regex.Pattern;

public class PatternNotFoundException extends RuntimeException {

     public static final String DEFAULT_MESSAGE = "Patter was not found in given image. Could not further process";

     public PatternNotFoundException() {
          super(DEFAULT_MESSAGE);
     }
}
