package com.theeye.api.v1.chess.engine.exception;

public class ChessEngineException extends RuntimeException {

     public static final String DEFAULT_MSG = "Couldnt process position analysis";

     public ChessEngineException() {
          super(DEFAULT_MSG);
     }

     public ChessEngineException(Throwable cause) {
          super(DEFAULT_MSG, cause);
     }

     public static ChessEngineException of(Throwable cause) {
          return new ChessEngineException(cause);
     }
}
