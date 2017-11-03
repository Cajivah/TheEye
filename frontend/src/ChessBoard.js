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
            <div>
                <Chessdiagram flip={false}
                              fen={this.position}
                              allowMoves={true}
                              lightSquareColor={'#F0D9B5'}
                              darkSquareColor={'#B58863'}
                              onMovePiece={ChessBoard.logMove}/>
            </div>
        );
    }
}

export default ChessBoard;