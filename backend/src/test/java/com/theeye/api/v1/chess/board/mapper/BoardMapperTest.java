package com.theeye.api.v1.chess.board.mapper;

import com.theeye.api.v1.chess.board.model.domain.ResolvingResult;
import com.theeye.api.v1.chess.board.model.dto.NewPositionDTO;
import org.junit.Test;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import static org.junit.Assert.*;

public class BoardMapperTest {

   BoardMapper sut = new BoardMapper() {
      @Override
      public NewPositionDTO toNewPositionDTO(ResolvingResult resolvingResult) {
         return null;
      }
   }

   @Test
   public void toBoard() throws Exception {


   }

   @Test
   public void toFEN() throws Exception {
   }

}