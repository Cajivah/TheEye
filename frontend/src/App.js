import React, { Component } from 'react';
import Chessboard from './ChessBoard';
import Webcam from 'react-webcam';
import './App.css';
import Modal from "react-modal";

const customStyles = {
    content : {
        top                   : '40%',
        left                  : '50%',
        right                 : 'auto',
        bottom                : 'auto',
        marginRight           : '-50%',
        transform             : 'translate(20%, -100%)'
    }
};

class App extends Component {
    subtitle;

    constructor(props) {
        super(props);
        this.state = {
            currentPosition: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
            modalIsOpen: false
        };
        this.openModal = this.openModal.bind(this);
        this.afterOpenModal = this.afterOpenModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }

    afterOpenModal() {

    }

    closeModal() {
        this.setState({modalIsOpen: false});
    }

    setRef = (webcam) => {
        this.webcam = webcam;
    };

    uploadWebcamCapture = () => {
        const imagesrc = this.webcam.getScreenshot();
    };

    render() {
        return (
            <div className="App">
                <Modal
                    isOpen={this.state.modalIsOpen}
                    onAfterOpen={this.afterOpenModal}
                    onRequestClose={this.closeModal}
                    style={customStyles}
                    contentLabel="Example Modal"
                    container={this}
                >
                    <h3>Promotion detected, pick a piece:</h3>
                    <form>
                        <div className="radio">
                            <label>
                                <input type="radio" value="queen" name="piece-select" checked={true} />
                                Queen
                            </label>
                        </div>
                        <div className="radio">
                            <label>
                                <input type="radio" value="rook" name="piece-select"/>
                                Rook
                            </label>
                        </div>
                        <div className="radio">
                            <label>
                                <input type="radio" value="knight" name="piece-select"/>
                                Knight
                            </label>
                        </div>
                        <div className="radio">
                            <label>
                                <input type="radio" value="bishop" name="piece-select"/>
                                Bishop
                            </label>
                        </div>
                    </form>
                    <button type="button" className="btn btn-green">Confirm</button>
                </Modal>
                <header> <h2>The Eye</h2> </header>
                <div className="container">
                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <button className="btn btn-green" title="Take a snapshot of epmty chessboard to let us configure tiles coordinates">Configure coords</button>
                            <button className="btn btn-grey btn-following" title="Take a snapshot of board with all pieces set up to let us get reference color samples">Configure colors</button>
                            <button className="btn btn-grey btn-following" title="Take a snapshot every time you make a move">Play</button>
                        </div>
                        <div className="col-md-3">
                            <button type="button" className="btn btn-green" onClick={this.openModal}>Submit snapshot</button>
                            <button type="button" className="btn btn-green btn-following" onClick={this.closeModal}>Finish</button>
                        </div>
                    </div>
                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <div className="advantage-gauge-holder">
                                <div className="advantage-gauge"/>
                                <div className="zero-gauge-marker"/>
                            </div>
                            <div className="chessdiagram-holder">
                                <Chessboard/>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <div className="row">
                                <div className="webcam-holder">
                                    <Webcam width={253} height={187} audio={false}/>
                                </div>
                            </div>
                            <div className="moves-holder">
                                <table className="table moves-table">
                                    <thead className="moves-table-head">
                                    <tr>
                                        <th>Move</th>
                                        <th className="middle-border">White</th>
                                        <th>Black</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>1.</td>
                                        <td className="middle-border">d4</td>
                                        <td>e5</td>
                                    </tr>
                                    <tr>
                                        <td>2.</td>
                                        <td className="middle-border">e3</td>
                                        <td>exd4</td>
                                    </tr>
                                    <tr>
                                        <td>3.</td>
                                        <td className="middle-border">exd4</td>
                                        <td>d5</td>
                                    </tr>
                                    <tr>
                                        <td>4.</td>
                                        <td className="middle-border">Bd3</td>
                                        <td>c5</td>
                                    </tr>
                                    <tr>
                                        <td>5.</td>
                                        <td className="middle-border">c3</td>
                                        <td>cxd4</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <div className="fen-text">
                                <span className="fen-span">
                                    {/*FEN:*/}
                                    FEN: r6P/ppp5/1k6/8/1P4rP/2N5/1PK5/6NR b - - 0 26
                                </span>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <button type="button" className="btn btn-green">Export game</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
