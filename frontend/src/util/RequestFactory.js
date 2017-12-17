import axios from 'axios';

const env = require("../env");

export default class RequestFactory {

    static buildCoordsRequest(image) {
        const url = `${env.API_URL}/chess/analysis/coords`;
        let payload = JSON.stringify({
            base64Image: image
        });
        return axios.post(url, payload,{
            headers: {
                'Content-Type': 'application/json',
            }
        });
    }

    static buildColorsRequest(image, tiles, corners) {
        const url = `${env.API_URL}/chess/analysis/colors`;
        let payload = JSON.stringify({
            image: {
                base64Image: image
            },
            tilesCornerPoints:tiles,
            chessboardCorners:corners
        });
        return axios.post(url, payload,{
            headers: {
                'Content-Type': 'application/json',
            }
        });
    }

    static buildMoveRequest(image, tiles, corners, colors, fen) {
        const url = `${env.API_URL}/chess/board/move`;
        let payload = JSON.stringify({
            chessboardImage: {
                base64Image: image
            },
            lastPosition: fen,
            referenceColors: colors,
            positions: {
                tilesCornerPoints: tiles,
                chessboardCorners: corners
            }
        });
        return axios.post(url, payload,{
            headers: {
                'Content-Type': 'application/json',
            }
        });
    }

    static buildScoreEvalRequest(fen) {
        const url = `${env.API_URL}/chess/engine/score`;
        let payload = JSON.stringify({
            fenDescription:fen
        });
        return axios.post(url, payload,{
            headers: {
                'Content-Type': 'application/json',
            }
        });
    }
}
