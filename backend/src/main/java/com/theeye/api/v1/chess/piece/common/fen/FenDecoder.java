package com.theeye.api.v1.chess.piece.common.fen;

import com.theeye.api.v1.chess.board.model.domain.Board;
import com.theeye.api.v1.chess.model.domain.Fen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.theeye.api.v1.chess.piece.common.fen.FenCodes.*;

@Component
public class FenDecoder {

     private final FenParser fenParser;

     @Autowired
     public FenDecoder(FenParser fenParser) {
          this.fenParser = fenParser;
     }

     public Board decode(Fen fen) {
          String[] fenSections = fen.getFenDescription().split(SECTION_DELIMITER);
          return Board.builder()
                      .tiles(fenParser.parsePositions(fenSections[POSITIONS_INDEX]))
                      .activeColour(fenParser.parseActiveColour(fenSections[ACTIVE_COLOUR_INDEX]))
                      .castling(fenParser.parseCastling(fenSections[CASTLING_INDEX]))
                      .enPassant(fenParser.parseEnPassant(fenSections[EN_PASSANT_INDEX]))
                      .halfmoveClock(fenParser.parseHalfmoveClock(fenSections[HALFMOVE_INDEX]))
                      .fullmoveNumber(fenParser.parseFullmoveNumber(fenSections[FULLMOVE_INDEX]))
                      .build();
     }
}
