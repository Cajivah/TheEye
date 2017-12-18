package com.theeye.api.v1.chess.board.service;

import com.theeye.api.v1.chess.board.model.enumeration.PlayerColor;
import com.theeye.api.v1.chess.board.exception.MoveDetectionException;
import com.theeye.api.v1.chess.board.model.domain.*;
import com.theeye.api.v1.chess.board.model.enumeration.MoveType;
import com.theeye.api.v1.chess.board.util.BoardUtils;
import com.theeye.api.v1.chess.image.analysis.model.enumeration.Occupancy;
import com.theeye.api.v1.chess.piece.model.domain.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.theeye.api.v1.chess.board.model.enumeration.PlayerColor.WHITE;
import static com.theeye.api.v1.chess.board.model.consts.CastlingConsts.*;
import static com.theeye.api.v1.chess.board.util.BoardPredicates.*;

@Service
public class BoardService {

     private final BoardDetailsUpdaterService boardDetailsUpdaterService;

     @Autowired
     public BoardService(BoardDetailsUpdaterService boardDetailsUpdaterService) {
          this.boardDetailsUpdaterService = boardDetailsUpdaterService;
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
          int newFullmoveCounter = boardDetailsUpdaterService.incrementFullmoveCounter(lastState);
          PlayerColor nextActivePlayer = boardDetailsUpdaterService.getNextActivePlayer(lastState);
          int newHalfmoveCounter = boardDetailsUpdaterService.incrementHalfmoveClock(lastState, tileChanges, moveType);
          PlayersCastlingStatuses newCastlingStatus =
                  boardDetailsUpdaterService.getNewCastlingStatus(
                          lastState.getCastling(),
                          moveType,
                          lastState.getActiveColor(),
                          tileChanges);
          String enPassant = boardDetailsUpdaterService.getEnPassantStatus(lastState, moveType, tileChanges);
          String lastMove = boardDetailsUpdaterService.getLastMoveAsLAN(lastState, moveType, tileChanges);
          return Board.builder()
                      .tiles(newTiles)
                      .fullmoveNumber(newFullmoveCounter)
                      .halfmoveClock(newHalfmoveCounter)
                      .activeColor(nextActivePlayer)
                      .castling(newCastlingStatus)
                      .enPassant(enPassant)
                      .lastMove(lastMove)
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
                                         .row(row)
                                         .column(column)
                                         .build();

          placePiece(newState, kingDestination, king);
     }

     private void repositionRookWhenCastling(Tile[][] newState, List<Piece> pickedUp, int row, int column) {
          Piece rook = pickedUp.stream()
                               .filter(IS_ROOK_PIECE)
                               .findFirst()
                               .orElseThrow(MoveDetectionException::new);

          Coords rookDestination = Coords.builder()
                                         .row(row)
                                         .column(column)
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
