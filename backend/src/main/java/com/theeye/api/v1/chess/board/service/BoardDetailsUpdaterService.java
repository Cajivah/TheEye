package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.*;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.fen.model.consts.FenCodes;
import com.theeye.api.v1.chess.piece.model.enumeration.PieceType;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.BLACK;
import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.WHITE;
import static com.theeye.api.v1.chess.board.util.BoardPredicates.*;
import static com.theeye.api.v1.chess.board.util.TileChangeAnalysisUtils.*;

@Service
public class BoardDetailsUpdaterService {

     public String getEnPassantStatus(Board lastBoard, MoveType moveType, List<TileChange> tileChanges) {
          Coords coords = null;
          if (moveType.equals(MoveType.REGULAR)) {
               coords = findEnPassantDetails(lastBoard, tileChanges);
          }
          return coords != null ? coords.toInvertedChessboardString() : FenCodes.EMPTY;
     }

     private Coords findEnPassantDetails(Board lastBoard, List<TileChange> tileChanges) {
          Optional<TileChange> unoccupiedByActive = tileChanges.stream()
                                                           .filter(UNOCCUPIED_BY_ACTIVE)
                                                           .findFirst();

          Optional<TileChange> occupiedByActive = tileChanges.stream()
                                                             .filter(OCCUPIED_BY_ACTIVE)
                                                             .findFirst();

          if (unoccupiedByActive.isPresent() && occupiedByActive.isPresent()) {
               TileChange changeToOccupied = occupiedByActive.get();
               TileChange changeToUnoccupied = unoccupiedByActive.get();
               return findEnPassantCoords(lastBoard, changeToOccupied, changeToUnoccupied);
          }
          return null;
     }

     @Nullable
     private Coords findEnPassantCoords(Board lastBoard, TileChange changeToOccupied, TileChange changeToUnoccupied) {
          Coords coordsOccupied = changeToOccupied.getCoords();
          Coords coordsUnoccupied = changeToUnoccupied.getCoords();
          if(!wasPawnMove(changeToUnoccupied) || !wasLongMove(coordsUnoccupied, coordsOccupied)) {
               return null;
          }
          PlayerColor active = changeToUnoccupied.getLastPiece().getOwner();
          if(hasOpponentPawnAroundInTheRow(lastBoard, coordsOccupied, active)) {
               int previousRow = active.equals(WHITE)
                       ? coordsOccupied.getRow() + 1
                       : coordsOccupied.getRow() - 1;
               return Coords.builder()
                            .row(previousRow)
                            .column(coordsOccupied.getColumn())
                            .build();
          }
          return null;
     }

     private boolean hasOpponentPawnAroundInTheRow(Board lastBoard, Coords coords, PlayerColor active) {
          PieceType opponentsPawn = active.equals(WHITE)
                  ? PieceType.PAWN_BLACK
                  : PieceType.PAWN_WHITE;
          Coords leftTile = new Coords(coords.getRow(), coords.getColumn() - 1);
          Coords rightTile = new Coords(coords.getRow(), coords.getColumn() + 1);

          return lastBoard.hasPieceOnCoords(leftTile, opponentsPawn)
                  || lastBoard.hasPieceOnCoords(rightTile, opponentsPawn);
     }

     private boolean wasLongMove(Coords coordsUnoccupied, Coords coordsOccupied) {
          return coordsUnoccupied.getColumn() == coordsOccupied.getColumn()
                  && (Math.abs(coordsOccupied.getRow() - coordsUnoccupied.getRow()) == 2);
     }

     private boolean wasPawnMove(TileChange change) {
          PieceType pieceType = change.getLastPiece().getPieceType();
          return pieceType.equals(PieceType.PAWN_BLACK) || pieceType.equals(PieceType.PAWN_WHITE);
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
