package com.theeye.api.v1.chess.board.moveresolver;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.*;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.fen.common.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.theeye.api.v1.chess.board.common.PlayerColor.BLACK;
import static com.theeye.api.v1.chess.board.common.PlayerColor.WHITE;
import static com.theeye.api.v1.chess.board.utils.BoardPredicates.*;
import static com.theeye.api.v1.chess.board.utils.TileChangeAnalysisUtils.*;

@Component
public class BoardDetailsUpdater {

     public String getEnPassantStatus(MoveType moveType, List<TileChange> tileChanges) {
          Coords coords = null;
          if (moveType.equals(MoveType.EN_PASSANT)) {
               coords = findEnPassantDetails(tileChanges);
          }
          return coords != null ? coords.toInvertedChessboardString() : FenCodes.EMPTY;
     }

     private Coords findEnPassantDetails(List<TileChange> tileChanges) {
          Optional<TileChange> opponentChange = tileChanges.stream()
                                                           .filter(UNOCCUPIED_BY_OPPONENT)
                                                           .findFirst();
          if (opponentChange.isPresent()) {
               TileChange change = opponentChange.get();
               return findEnPassantCoords(change);
          }
          return null;
     }

     private Coords findEnPassantCoords(TileChange change) {
          Coords coords = change.getCoords();
          PlayerColor opponentColor = change.getLastPiece().getOwner();
          int column = coords.getColumn();
          int previousColumn = opponentColor.equals(WHITE)
                  ? column + 1
                  : column - 1;
          return Coords.builder()
                       .row(coords.getRow())
                       .column(previousColumn)
                       .build();
     }

     public int incrementHalfmoveClock(Board lastState, List<TileChange> tileChanges, MoveType moveType) {
          int halfmoveClock = lastState.getHalfmoveClock();
          switch (moveType) {
               case REGULAR:
                    return incrementIfNotAPawnMove(halfmoveClock, tileChanges);
               case TAKE:
               case EN_PASSANT:
                    return 0;
               case CASTLE_QUEEN:
               case CASTLE_KING:
                    return ++halfmoveClock;
               case PROMOTE:
               case UNKNOWN:
               default:
                    throw new MoveDetectionException();
          }
     }

     private int incrementIfNotAPawnMove(int halfmoveClock, List<TileChange> tileChanges) {

          Predicate<PieceType> pawnMove = pieceType -> pieceType.equals(PieceType.PAWN_BLACK) || pieceType.equals(PieceType.PAWN_WHITE);
          boolean wasPawnMove = tileChanges.stream()
                                           .map(change -> change.getLastPiece().getPieceType())
                                           .anyMatch(pawnMove);
          return wasPawnMove
                  ? 0
                  : ++halfmoveClock;
     }

     public PlayerColor getNextActivePlayer(Board lastState) {
          return lastState.getActiveColor().equals(WHITE)
                  ? BLACK
                  : WHITE;
     }

     public int incrementFullmoveCounter(Board lastState) {
          int fullmoveNumber = lastState.getFullmoveNumber();
          if (lastState.getActiveColor().equals(WHITE)) {
               return ++fullmoveNumber;
          } else {
               return fullmoveNumber;
          }
     }

     public PlayersCastlingStatuses getNewCastlingStatus(PlayersCastlingStatuses castling,
                                                         MoveType moveType,
                                                         PlayerColor activePlayer,
                                                         List<TileChange> tileChanges) {

          CastlingStatus castlingStatus = activePlayer.equals(WHITE)
                  ? castling.getWhite()
                  : castling.getBlack();

          if (!castlingStatus.canCastle()) {
               return castling;
          } else {
               CastlingStatus newCastlingStatus = findNewCastlingStatus(castlingStatus, moveType, activePlayer, tileChanges);
               return updateDependingOnActivePlayer(castling, newCastlingStatus, activePlayer);
          }
     }

     private CastlingStatus findNewCastlingStatus(CastlingStatus castlingStatus,
                                                  MoveType moveType,
                                                  PlayerColor activePlayer,
                                                  List<TileChange> tileChanges) {
          boolean canCastleKingSide = castlingStatus.isKingSideCastle();
          boolean canCastleQueenSide = castlingStatus.isQueenSideCastle();

          if (WAS_CASTLING.test(moveType) || movedKing(tileChanges)) {
               canCastleKingSide = false;
               canCastleQueenSide = false;
          } else if (WAS_REGULAR_OR_TAKE.test(moveType)) {
               if (movedKingSideRook(tileChanges, activePlayer)) {
                    canCastleKingSide = false;
               } else if (movedQueenSideRook(tileChanges, activePlayer)) {
                    canCastleQueenSide = false;
               }
          }

          return CastlingStatus.builder()
                               .kingSideCastle(canCastleKingSide)
                               .queenSideCastle(canCastleQueenSide)
                               .build();
     }

     private PlayersCastlingStatuses updateDependingOnActivePlayer(PlayersCastlingStatuses castling,
                                                                   CastlingStatus newCastlingStatus,
                                                                   PlayerColor activePlayer) {
          CastlingStatus newStatusWhite;
          CastlingStatus newStatusBlack;

          if (activePlayer.equals(WHITE)) {
               newStatusWhite = newCastlingStatus;
               newStatusBlack = castling.getBlack();
          } else {
               newStatusWhite = castling.getWhite();
               newStatusBlack = newCastlingStatus;
          }

          return PlayersCastlingStatuses.builder()
                                        .white(newStatusWhite)
                                        .black(newStatusBlack)
                                        .build();
     }
}
