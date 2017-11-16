package com.theeye.api.v1.chess.board.validation;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.TileChange;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class ResolvingValidator {

     private List<TileChange> changes;
     @Getter boolean valid = true;

     private static final int MIN_CHANGES = 2;
     private static final int MAX_CHANGES = 2;

     public static ResolvingValidator of(List<TileChange> changes) {
          return new ResolvingValidator(changes, true);
     }

     public ResolvingValidator validateChangesCount() {
          if(!valid) {
               return this;
          }
          int changesCount = changes.size();
          if(changesCount < MIN_CHANGES || changesCount > MAX_CHANGES) {
               valid = false;
          }
          return this;
     }

     public void throwIfInvalid() {
          if(!valid) {
               throw new MoveDetectionException();
          }
     }
}
