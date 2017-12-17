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
                              fen={this.props.position}
                              allowMoves={true}
                              lightSquareColor={'#ffffff'}
                              darkSquareColor={'#5f5f5f'}
                              onMovePiece={ChessBoard.logMove}/>

        );
    }
}

export default ChessBoard;