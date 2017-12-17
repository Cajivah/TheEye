import {Component} from "react";
import * as React from "react";

export default class PromotionChangeComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedPromotion: 'queen'
        }
    }

    handlePromotionChange(e) {
        this.setState({selectedPromotion: e.target.value});
    }

    handleSubmit() {
        console.log(this.state.selectedPromotion);
        this.props.choosePromotion(this.state.selectedPromotion);
    }

    render() {
        return (
            <div>
                <h3>Promotion detected, pick a piece:</h3>
                <form>
                    <div className="radio">
                        <label>
                            <input type="radio" value="queen" name="piece-select" checked={this.state.selectedPromotion === 'queen'} onChange={this.handlePromotionChange.bind(this)}/>
                            Queen
                        </label>
                    </div>
                    <div className="radio">
                        <label>
                            <input type="radio" value="rook" name="piece-select" checked={this.state.selectedPromotion === 'rook'} onChange={this.handlePromotionChange.bind(this)}/>
                            Rook
                        </label>
                    </div>
                    <div className="radio">
                        <label>
                            <input type="radio" value="knight" name="piece-select" checked={this.state.selectedPromotion === 'knight'} onChange={this.handlePromotionChange.bind(this)}/>
                            Knight
                        </label>
                    </div>
                    <div className="radio">
                        <label>
                            <input type="radio" value="bishop" name="piece-select" checked={this.state.selectedPromotion === 'bishop'} onChange={this.handlePromotionChange.bind(this)}/>
                            Bishop
                        </label>
                    </div>
                </form>
                <button type="submit" className="btn btn-green" onClick={this.handleSubmit.bind(this)}>Confirm</button>
            </div>
        );
    }
}