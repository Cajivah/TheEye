import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Chessdiagram from 'react-chessdiagram';

class ChessBoard extends Component {

    static propTypes = {
      position: PropTypes.string
    };

    static logMove(piece, fromSquare, toSquare) {
        let message = 'You moved ' + piece + fromSquare + ' to ' + toSquare + ' !';
        console.log(message);
    }

    render() {
        return (
                <Chessdiagram flip={false}
                              fen={'rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1'}
                              allowMoves={true}
                              lightSquareColor={'#ffffff'}
                              darkSquareColor={'#5f5f5f'}
                              onMovePiece={ChessBoard.logMove}/>

        );
    }
}

export default ChessBoard;