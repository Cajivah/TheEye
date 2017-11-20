package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.board.common.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.*;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.moveresolver.BoardDetailsUpdater;
import com.theeye.api.v1.chess.board.utils.BoardUtils;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.board.common.PlayerColor.WHITE;
import static com.theeye.api.v1.chess.board.model.consts.CastlingConsts.*;
import static com.theeye.api.v1.chess.board.utils.BoardPredicates.*;

@Service
public class BoardService {

     private final BoardDetailsUpdater boardDetailsUpdater;

     @Autowired
     public BoardService(BoardDetailsUpdater boardDetailsUpdater) {
          this.boardDetailsUpdater = boardDetailsUpdater;
     }

     public Board doMove(Board lastState, List<TileChange> tileChanges, MoveType moveType) {
          Tile[][] newTiles;
          switch (moveType) {
               case REGULAR:
               case TAKE:
                    newTiles = doRegularMove(lastState, tileChanges);
                    break;
               case CASTLE_QUEEN:
                    newTiles = doCastleQueenSide(lastState, tileChanges);
                    break;
               case CASTLE_KING:
                    newTiles = doCastleKingSide(lastState, tileChanges);
                    break;
               case EN_PASSANT:
                    newTiles = doEnPassant(lastState, tileChanges);
                    break;
               case PROMOTE:
               case UNKNOWN:
               default:
                    throw new MoveDetectionException();
          }
          return createBoardWithUpdatedFen(newTiles, lastState, tileChanges, moveType);
     }

     private Board createBoardWithUpdatedFen(Tile[][] newTiles,
                                             Board lastState,
                                             List<TileChange> tileChanges,
                                             MoveType moveType) {
          int newFullmoveCounter = boardDetailsUpdater.incrementFullmoveCounter(lastState);
          PlayerColor nextActivePlayer = boardDetailsUpdater.getNextActivePlayer(lastState);
          int newHalfmoveCounter = boardDetailsUpdater.incrementHalfmoveClock(lastState, tileChanges, moveType);
          PlayersCastlingStatuses newCastlingStatus =
                  boardDetailsUpdater.getNewCastlingStatus(
                          lastState.getCastling(),
                          moveType,
                          lastState.getActiveColor(),
                          tileChanges);
          String enPassant = boardDetailsUpdater.getEnPassantStatus(moveType, tileChanges);
          return Board.builder()
                      .tiles(newTiles)
                      .fullmoveNumber(newFullmoveCounter)
                      .halfmoveClock(newHalfmoveCounter)
                      .activeColor(nextActivePlayer)
                      .castling(newCastlingStatus)
                      .enPassant(enPassant)
                      .build();
     }


     private Tile[][] doRegularMove(Board lastState, List<TileChange> tileChanges) {
          Tile[][] newState = BoardUtils.shallowCopyTiles(lastState.getTiles());
          Occupancy activePlayerOccupancy =
                  lastState.getActiveColor().equals(WHITE)
                          ? Occupancy.OCCUPIED_BY_WHITE
                          : Occupancy.OCCUPIED_BY_BLACK;

          Piece pickedUp = tileChanges.stream()
                                      .filter(CHANGED_TO_UNOCCUPIED)
                                      .findFirst()
                                      .map(change -> pickUpPiece(newState, change.getCoords()))
                                      .orElseThrow(MoveDetectionException::new);
          tileChanges.stream()
                     .filter(change -> change.getNewOccupancy().equals(activePlayerOccupancy))
                     .forEach(change -> placePiece(newState, change.getCoords(), pickedUp));
          return newState;
     }

     private Tile[][] doCastleQueenSide(Board lastState, List<TileChange> tileChanges) {
          return doCastle(lastState, tileChanges, QUEEN_SIDE_KING_DEST_COL, QUEEN_SIDE_ROOK_DEST_COL);
     }

     private Tile[][] doCastle(Board lastState, List<TileChange> tileChanges, int kingDest, int rookDest) {
          Tile[][] newState = BoardUtils.shallowCopyTiles(lastState.getTiles());
          PlayerColor activePlayer = lastState.getActiveColor();
          List<Piece> pickedUp = tileChanges.stream()
                                            .filter(CHANGED_TO_UNOCCUPIED)
                                            .map(change -> pickUpPiece(newState, change.getCoords()))
                                            .collect(Collectors.toList());
          int objectiveRow = activePlayer.getObjectiveRow();

          repositionRookWhenCastling(newState, pickedUp, objectiveRow, rookDest);
          repositionKingWhenCastling(newState, pickedUp, objectiveRow, kingDest);

          return newState;
     }

     private void repositionKingWhenCastling(Tile[][] newState, List<Piece> pickedUp, int row, int column) {
          Piece king = pickedUp.stream()
                               .filter(IS_KING_PIECE)
                               .findFirst()
                               .orElseThrow(MoveDetectionException::new);

          Coords kingDestination = Coords.builder()
                                         .row(column)
                                         .column(row)
                                         .build();

          placePiece(newState, kingDestination, king);
     }

     private void repositionRookWhenCastling(Tile[][] newState, List<Piece> pickedUp, int row, int column) {
          Piece rook = pickedUp.stream()
                               .filter(IS_ROOK_PIECE)
                               .findFirst()
                               .orElseThrow(MoveDetectionException::new);

          Coords rookDestination = Coords.builder()
                                         .row(column)
                                         .column(row)
                                         .build();

          placePiece(newState, rookDestination, rook);
     }

     private Tile[][] doCastleKingSide(Board lastState, List<TileChange> tileChanges) {
          return doCastle(lastState, tileChanges, KING_SIDE_KING_DEST_COL, KING_SIDE_ROOK_DEST_COL);
     }

     private Tile[][] doEnPassant(Board lastState, List<TileChange> tileChanges) {
          List<TileChange> activeMoves = tileChanges.stream()
                                                    .filter(ACTIVE_MOVE_CHANGES)
                                                    .collect(Collectors.toList());
          tileChanges.stream()
                     .filter(UNOCCUPIED_BY_OPPONENT)
                     .forEach(change -> removePiece(lastState, change.getCoords()));

          return doRegularMove(lastState, activeMoves);
     }

     private void removePiece(Board lastState, Coords coords) {
          lastState.getTileAt(coords).unoccupy();
     }

     private Piece pickUpPiece(Tile[][] lastState, Coords coords) {
          Tile tile = lastState[coords.getRow()][coords.getColumn()];
          Piece pickedUpPiece = tile.getPiece();
          tile.unoccupy();
          return pickedUpPiece;
     }

     private void placePiece(Tile[][] lastState, Coords coords, Piece pickedUp) {
          lastState[coords.getRow()][coords.getColumn()] = Tile.of(pickedUp);
     }
}
