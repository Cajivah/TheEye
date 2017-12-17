import React, { Component } from 'react';
import Chessboard from './partials/ChessBoard';
import Webcam from 'react-webcam';
import './App.css';
import FenTranslator from './util/FenTranslator';
import Modal from "react-modal";
import PromotionChangeComponent from './partials/PromotionChangeComponent';
import RequestFactory from "./util/RequestFactory"


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
            currentPosition: 'P2qr1k1/1B1n1ppp/4pn2/2Pp4/3P4/P1B1P3/5PPP/R2QK1NR b KQ - 0 17',
            modalIsOpen: false,
            imageStage: ImageStageEnum.COORDS
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

    capture = () => {
        return this.webcam.getScreenshot();
    };

    choosePromotion(selectedPromotion){
        let newPosition = FenTranslator.updateFenAfterPromotion(this.state.currentPosition, selectedPromotion);
        this.setState({currentPosition: newPosition});
        this.closeModal();
    };

    handleImageSubmit() {
        var image = this.capture();
        switch (this.state.imageStage) {
            case ImageStageEnum.COORDS:
                this.doCoordsRequest(image);
                break;
            case ImageStageEnum.COLORS:
                break;
            case ImageStageEnum.PLAY:
                break;
        }
    }

    doCoordsRequest(image) {
        const request = RequestFactory.buildCoordsRequest(image);
        request
            .then(function (response) {
                console.log(response)
            })
            .catch(function (error) {
                console.log(error)
            })
    }

    handleFinish() {

    }

    render() {
        return (
            <div className="App">
                <Modal
                    isOpen={this.state.modalIsOpen}
                    onAfterOpen={this.afterOpenModal}
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
                            <button className="btn btn-green" title="Take a snapshot of empty chessboard to let us configure tiles coordinates">Configure coords</button>
                            <button className="btn btn-grey btn-following" title="Take a snapshot of board with all pieces set up to let us get reference color samples">Configure colors</button>
                            <button className="btn btn-grey btn-following" title="Take a snapshot every time you make a move">Play</button>
                        </div>
                        <div className="col-md-3">
                            <button type="button" className="btn btn-green" onClick={this.handleImageSubmit.bind(this)}>Submit snapshot</button>
                            <button type="button" className="btn btn-green btn-following" onClick={this.handleFinish}>Finish</button>
                        </div>
                    </div>
                    <div className="row top-margin">
                        <div className="col-md-5 col-md-offset-2">
                            <div className="advantage-gauge-holder">
                                <div className="advantage-gauge"/>
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
                            <button type="button" className="btn btn-green">Export game</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default App;
