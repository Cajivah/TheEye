import React, { Component } from 'react';
import Chessboard from './partials/ChessBoard';
import Webcam from 'react-webcam';
import './App.css';
import FenTranslator from './util/FenTranslator';
import Modal from "react-modal";
import PromotionChangeComponent from './partials/PromotionChangeComponent';
import RequestFactory from "./util/RequestFactory"
import PgnUtils from "./util/PgnUtils";
import fileDownload from 'js-file-download';

const modalStyle = {
    content : {
        top                   : '40%',
        left                  : '50%',
        right                 : 'auto',
        bottom                : 'auto',
        marginRight           : '-50%',
        transform             : 'translate(20%, -100%)'
    }
};


const ImageStageEnum = Object.freeze({'COORDS': 0, 'COLORS': 1, 'PLAY': 2});

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentPosition: ' ',
            modalIsOpen: false,
            imageStage: ImageStageEnum.COORDS,
            tiles:null,
            corners:null,
            colors:null,
            score:0,
            moves:[{moveNumber: 1, white: '...', black: ''}]
        };


        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.computeAdvantageHeight = this.computeAdvantageHeight.bind(this);
    }


    computeAdvantageHeight() {
        const score = this.state.score < -8
            ? -8
            : this.state.score > 8
                ? 8
                : this.state.score;
        console.log(score);
        return 50 - (score * 50 / 8)

    }

    openModal() {
        this.setState({modalIsOpen: true});
    }

    closeModal() {
        this.setState({modalIsOpen: false});
    }

    setRef = (webcam) => {
        this.webcam = webcam;
    };

    capture = () => {
        return this.webcam.getScreenshot();
    };

    choosePromotion(selectedPromotion){
        let newPosition = FenTranslator.updateFenAfterPromotion(this.state.currentPosition, selectedPromotion);
        this.setState({currentPosition: newPosition});
        this.closeModal();
    };

    handleImageSubmit() {
        const image = this.capture();
        switch (this.state.imageStage) {
            case ImageStageEnum.COORDS:
                this.doCoordsRequest(image);
                break;
            case ImageStageEnum.COLORS:
                this.doColorsRequest(image);
                break;
            case ImageStageEnum.PLAY:
                this.doMoveRequest(image);
                this.doScoreRequest();
                break;
        }
    }

    doCoordsRequest(image) {
        const request = RequestFactory.buildCoordsRequest(image);
        request
            .then(response => {
                this.setState(
                    {
                        currentPosition:FenTranslator.STARTING_FEN,
                        imageStage:ImageStageEnum.COLORS,
                        tiles:response.data.tilesCornerPoints,
                        corners:response.data.chessboardCorners
                    });
                console.log('@doCoordsRequest');
                console.log(response)
            })
            .catch(error =>  {
                this.handleFinish();
                console.log('@doCoordsRequest');
                console.log(error)
            })
    }

    doColorsRequest(image) {
        const request = RequestFactory.buildColorsRequest(image, this.state.tiles, this.state.corners);
        request
            .then(response => {
                this.setState(
                    {
                        currentPosition:FenTranslator.STARTING_FEN,
                        imageStage:ImageStageEnum.PLAY,
                        colors:response.data
                    });
                console.log('@doColorsRequest');
                console.log(response)
            })
            .catch(error => {
                console.log('@doColorsRequest');
                console.log(error)
            })
    }

    doMoveRequest(image) {
        const request =
            RequestFactory.buildMoveRequest(
                image,
                this.state.tiles,
                this.state.corners,
                this.state.colors,
                this.state.currentPosition);

        request
            .then(response => {
                let fen = response.data.newPosition;
                let move = response.data.move;
                var newMovesArray = PgnUtils.appendMove(this.state.moves, move);
                this.setState({
                    currentPosition: fen,
                    moves: newMovesArray
                });
                if(FenTranslator.isPromotion(fen)) {
                    this.openModal();
                }
                console.log('@doMoveRequest');
                console.log(response)
            })
            .catch(error => {
                console.log('@doMoveRequest');
                console.log(error)
            })
    }

    doScoreRequest() {
        const request = RequestFactory.buildScoreEvalRequest(this.state.currentPosition);
        request
            .then(response => {
                this.setState({
                   score:response.data.centipawnScore
                });
                console.log('@doScoreRequest');
                console.log(response);
                console.log(this.state)
            })
            .catch(error => {
                this.setState({
                   score:0
                });
                console.log('@doScoreRequest');
                console.log(error)
            })
    }

    handleFinish() {
        this.setState(
            {
                currentPosition:" ",
                imageStage:ImageStageEnum.COORDS,
                tiles:null,
                corners:null,
                colors:null,
                moves:[{moveNumber: 1, white: '...', black: ''}],
                score:0
            });
        console.log('@handleFinish: Resetting setup')
    }

    render() {
        var pgnMoves = this.state.moves;
        return (
            <div className="App">
                <Modal
                    isOpen={this.state.modalIsOpen}
                    onRequestClose={this.closeModal}
                    style={modalStyle}
                    contentLabel="Example Modal"
                    container={this}
                >
                    <PromotionChangeComponent choosePromotion={this.choosePromotion.bind(this)}/>
                </Modal>

                <header> <h2>The Eye</h2> </header>
                <div className="container">
                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <button className={this.state.imageStage === ImageStageEnum.COORDS ? "btn btn-green" : "btn"}
                                    title="Take a snapshot of empty chessboard to let us configure tiles coordinates">Configure coords</button>
                            <button className={this.state.imageStage === ImageStageEnum.COLORS ? "btn btn-green btn-following" : "btn btn-grey btn-following"}
                                    title="Take a snapshot of board with all pieces set up to let us get reference color samples">Configure colors</button>
                            <button className={this.state.imageStage === ImageStageEnum.PLAY ? "btn btn-green btn-following" : "btn btn-grey btn-following"}
                                    title="Take a snapshot every time you make a move">Play</button>
                        </div>
                        <div className="col-md-3">
                            <button type="button" className="btn btn-green" onClick={this.handleImageSubmit.bind(this)}>Submit snapshot</button>
                            <button type="button" className="btn btn-green btn-following" onClick={this.handleFinish.bind(this)}>Finish</button>
                        </div>
                    </div>
                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <div className="advantage-gauge-holder">
                                <div className="advantage-gauge"
                                     style={{"height":50 - ((this.state.score > 7.8 ? 7.8 : this.state.score < -7.8 ? -7.8 : this.state.score) * 50 / 8) + "%"}}/>
                                <div className="zero-gauge-marker"/>
                            </div>
                            <div className="chessdiagram-holder">
                                <Chessboard position={this.state.currentPosition}/>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <div className="row">
                                <div className="webcam-holder">
                                    <Webcam width={253}
                                            height={187}
                                            audio={false}
                                            screenshotFormat="image/jpeg"
                                            ref={this.setRef}/>
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
                                    {pgnMoves.map(App.renderPgn)}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <div className="fen-text">
                                <span className="fen-span">
                                    FEN: {this.state.currentPosition}
                                </span>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <button type="button" className="btn btn-green" onClick={this.handlePgnExport.bind(this)}>Export game</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    handlePgnExport() {
        fileDownload(PgnUtils.exportPgn(this.state.moves), 'game.txt');
    }

    static renderPgn(move, index) {
        return (
            <tr key={index}>
                <td>{move.moveNumber}</td>
                <td>{move.white}</td>
                <td>{move.black}</td>
            </tr>
        )
    }
}

export default App;