package com.theeye.api.v1.chess.board.exception;

public class MoveDetectionException extends RuntimeException {
     private static final String DEFAULT_MESSAGE = "Could not detect move, try to resend image!";

     public MoveDetectionException(Throwable throwable) {
          super(DEFAULT_MESSAGE, throwable);
     }

     public MoveDetectionException() {
          super(DEFAULT_MESSAGE);
     }
}
