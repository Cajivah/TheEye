package com.theeye.api.v1.config;

import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.image.analysis.exception.IncorrectBoundsException;
import com.theeye.api.v1.chess.image.analysis.exception.PatternNotFoundException;
import com.theeye.api.v1.chess.piece.exception.InvalidFenException;
import com.theeye.api.v1.common.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static com.theeye.api.v1.common.model.ErrorCodes.*;

@ControllerAdvice
public class RestControllerExceptionHandler {

     @ExceptionHandler(value={PatternNotFoundException.class})
     public ResponseEntity<Object> handlePatternNotFoundException(
             PatternNotFoundException ex, WebRequest request) {

          ErrorDTO error = ErrorDTO.builder()
                                   .message(ex.getMessage())
                                   .code(IMAGE_PREFIX + PATTERN_NOT_FOUND)
                                   .build();

          return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(value={IncorrectBoundsException.class})
     public ResponseEntity<Object> handleIncorrectBoundsException(IncorrectBoundsException ex, WebRequest request) {

          ErrorDTO error = ErrorDTO.builder()
                                   .message(ex.getMessage())
                                   .code(IMAGE_PREFIX + INCORRECT_BOUNDS)
                                   .build();
          return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(value={InvalidFenException.class})
     public ResponseEntity<Object> handleInvalidFenException(InvalidFenException ex, WebRequest request) {

          ErrorDTO error = ErrorDTO.builder()
                                   .message(ex.getMessage())
                                   .code("101")
                                   .build();
          return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(value={MoveDetectionException.class})
     public ResponseEntity<Object> handleMoveDetectionException(MoveDetectionException ex, WebRequest request) {

          ErrorDTO error = ErrorDTO.builder()
                                   .message(ex.getMessage())
                                   .code("301")
                                   .build();

          return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
     }
}
