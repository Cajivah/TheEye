package com.theeye.api.v1.chess.analysis.exception;

public class IncorrectBoundsException extends RuntimeException {

     public static final String DEFAULT_MESSAGE = "Chessboard bound bounds not be used to determine corners";

     public IncorrectBoundsException() {
          super(DEFAULT_MESSAGE);
     }
}
