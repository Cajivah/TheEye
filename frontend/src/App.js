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
              <header> <h2>The Eye</h2> </header>
              <div class="container">
                  <div class="row top-margin">
                      <div class="col-md-5 col-md-offset-2">
                              <button class="btn btn-green" title="Take a snapshot of epmty chessboard to let us configure tiles coordinates">Configure coords</button>
                              <button class="btn btn-grey btn-following" title="Take a snapshot of board with all pieces set up to let us get reference color samples">Configure colors</button>
                              <button class="btn btn-grey btn-following" title="Take a snapshot every time you make a move">Play</button>
                      </div>
                      <div class="col-md-3">
                          <button type="button" class="btn btn-green">Submit snapshot</button>
                          <button type="button" class="btn btn-green btn-following">Finish</button>
                      </div>
                  </div>
                  <div class="row top-margin">
                      <div class="col-md-5 col-md-offset-2">
                          <div class="advantage-gauge-holder">
                              <div class="advantage-gauge"/>
                              <div class="zero-gauge-marker"/>
                          </div>
                          <div class="chessdiagram-holder">
                              <Chessboard/>
                          </div>
                      </div>
                        <div class="col-md-3">
                            <div class="row">
                                <div class="webcam-holder">
                                    <Webcam width={253} height={187} audio={false}/>
                                </div>
                            </div>
                            <div class="moves-holder">
                                <table class="table moves-table">
                                    <thead class="moves-table-head">
                                    <tr>
                                        <th>Move</th>
                                        <th class="middle-border">White</th>
                                        <th>Black</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>1.</td>
                                        <td class="middle-border">e4</td>
                                        <td></td>
                                        {/*<td>1.</td>*/}
                                        {/*<td class="middle-border">e4</td>*/}
                                        {/*<td>e5</td>*/}
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                  </div>

                  <div class="row top-margin">
                      <div class="col-md-5 col-md-offset-2">
                          <div class="fen-text">
                              <span class="fen-span">
                                  {/*FEN:*/}
                                  FEN: rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq 0 1
                              </span>
                          </div>
                      </div>
                      <div class="col-md-3">
                        <button type="button" class="btn btn-green">Export game</button>
                      </div>
                  </div>
              </div>
          </div>
      );
  }
}

export default App;
