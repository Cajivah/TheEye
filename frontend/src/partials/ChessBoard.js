import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Chessdiagram from 'react-chessdiagram';

class ChessBoard extends Component {

    static propTypes = {
      position: PropTypes.string
    };

    setRef = (chessdiagram) => {
        this.chessdiagram = chessdiagram;
    };

    render() {
        return (
                <Chessdiagram flip={false}
                              fen={this.props.position}
                              allowMoves={true}
                              lightSquareColor={'#ffffff'}
                              darkSquareColor={'#5f5f5f'}
                              ref={this.setRef}/>

        );
    }
}

export default ChessBoard;