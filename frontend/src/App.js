import React, { Component } from 'react';
import Chessboard from './ChessBoard';
import Webcam from 'react-webcam';
import './App.css';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            currentPosition: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
        }
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
          <header> Chessify </header>
          <Chessboard position={this.currentPosition}/>
          <button

              onClick={this.uploadWebcamCapture}>
              Upload
          </button>
          <Webcam height={100} audio={false}/>
      </div>
    );
  }
}

export default App;
