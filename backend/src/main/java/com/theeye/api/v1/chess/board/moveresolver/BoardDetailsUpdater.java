package com.theeye.api.v1.chess.board.moveresolver;

import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.*;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.fen.common.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;
import org.jetbrains.annotations.Nullable;
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
          if (moveType.equals(MoveType.REGULAR)) {
               coords = findEnPassantDetails(tileChanges);
          }
          return coords != null ? coords.toInvertedChessboardString() : FenCodes.EMPTY;
     }

     private Coords findEnPassantDetails(List<TileChange> tileChanges) {
          Optional<TileChange> unoccupiedByActive = tileChanges.stream()
                                                           .filter(UNOCCUPIED_BY_ACTIVE)
                                                           .findFirst();

          Optional<TileChange> occupiedByActive = tileChanges.stream()
                                                             .filter(OCCUPIED_BY_ACTIVE)
                                                             .findFirst();
          if (unoccupiedByActive.isPresent() && occupiedByActive.isPresent()) {
               TileChange changeToOccupied = occupiedByActive.get();
               TileChange changeToUnoccupied = unoccupiedByActive.get();
               return findEnPassantCoords(changeToOccupied, changeToUnoccupied);
          }
          return null;
     }

     @Nullable
     private Coords findEnPassantCoords(TileChange changeToOccupied, TileChange changeToUnoccupied) {
          Coords coordsOccupied = changeToOccupied.getCoords();
          Coords coordsUnoccupied = changeToUnoccupied.getCoords();
          if(!wasPawnMove(changeToUnoccupied) || !wasLongMove(coordsUnoccupied, coordsOccupied)) {
               return null;
          }
          PlayerColor active = changeToUnoccupied.getLastPiece().getOwner();
          int previousRow = active.equals(WHITE)
                  ? coordsOccupied.getRow() - 1
                  : coordsOccupied.getRow() + 1;
          return Coords.builder()
                       .row(previousRow)
                       .column(coordsOccupied.getColumn())
                       .build();
     }

     private boolean wasLongMove(Coords coordsUnoccupied, Coords coordsOccupied) {
          return coordsUnoccupied.getColumn() == coordsOccupied.getColumn()
                  && (coordsOccupied.getRow() - coordsUnoccupied.getRow()) == 2;
     }

     private boolean wasPawnMove(TileChange change) {
          PieceType pieceType = change.getLastPiece().getPieceType();
          return pieceType.equals(PieceType.PAWN_BLACK) || pieceType.equals(PieceType.PAWN_WHITE);
     }

     private boolean onTheSameColumn(Coords coords1, Coords coords2) {
          return coords1.getColumn() == coords2.getColumn();
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
          if (lastState.getActiveColor().equals(BLACK)) {
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
