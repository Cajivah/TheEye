package com.theeye.api.v1.chess.piece.exception;

public class InvalidFenException extends RuntimeException {
     public static final String MESSAGE_TEMPLATE = "Invalid FEN Code: %c";

     private InvalidFenException(char fenCode, Throwable throwable) {
          super(String.format(MESSAGE_TEMPLATE, fenCode), throwable);
     }

     public InvalidFenException(char fenCode) {
          super(String.format(MESSAGE_TEMPLATE, fenCode));
     }

     public static InvalidFenException of(char fenCode, Throwable throwable) {
          return new InvalidFenException(fenCode, throwable);
     }

     public static InvalidFenException of(char fenCode) {
          return new InvalidFenException(fenCode);
     }
}
