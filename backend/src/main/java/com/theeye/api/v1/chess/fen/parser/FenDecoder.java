package com.theeye.api.v1.chess.fen.parser;

import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.fen.common.FenCodes;
import com.theeye.api.v1.chess.fen.model.domain.Fen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FenDecoder {

     private final FenParser fenParser;

     @Autowired
     public FenDecoder(FenParser fenParser) {
          this.fenParser = fenParser;
     }

     public Board decode(Fen fen) {
          String[] fenSections = fen.getFenDescription().split(FenCodes.SECTION_DELIMITER);
          return Board.builder()
                      .tiles(fenParser.parsePositions(fenSections[FenCodes.POSITIONS_INDEX]))
                      .activeColor(fenParser.parseActiveColor(fenSections[FenCodes.ACTIVE_COLOUR_INDEX]))
                      .castling(fenParser.parseCastling(fenSections[FenCodes.CASTLING_INDEX]))
                      .enPassant(fenParser.parseEnPassant(fenSections[FenCodes.EN_PASSANT_INDEX]))
                      .halfmoveClock(fenParser.parseHalfmoveClock(fenSections[FenCodes.HALFMOVE_INDEX]))
                      .fullmoveNumber(fenParser.parseFullmoveNumber(fenSections[FenCodes.FULLMOVE_INDEX]))
                      .build();
     }
}
