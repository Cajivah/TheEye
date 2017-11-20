package com.theeye.api.v1.chess.board.validation;

import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class ResolvingValidator {

     private static final int MIN_CHANGES = 2;
     private static final int MAX_CHANGES = 2;
     @Getter
     boolean valid = true;
     private List<TileChange> changes;

     public static ResolvingValidator of(List<TileChange> changes) {
          return new ResolvingValidator( true, changes);
     }

     public ResolvingValidator validateChangesCount() {
          if (!valid) {
               return this;
          }
          int changesCount = changes.size();
          if (changesCount < MIN_CHANGES || changesCount > MAX_CHANGES) {
               valid = false;
          }
          return this;
     }

     public void throwIfInvalid() {
          if (!valid) {
               throw new MoveDetectionException();
          }
     }
}
