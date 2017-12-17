const CODES = [['queen', 'q'], ['rook', 'r'], ['bishop', 'b'], ['knight', 'n']];
const COLORS = ['white', 'black'];
const FEN_SECTION_DELIMITER = ' ';
const FEN_POSITION_DELIMITER = '/';

const FEN_POSITIONS_SECTION = 0;
export default class FenTranslator {



    static getPositionsAsRowArray(fen) {
        return fen.split(FEN_POSITION_DELIMITER);
    };

    static splitFenSections(fen) {
        return fen.split(FEN_SECTION_DELIMITER);
    }

    static translateColor(code, color) {
        if(color === COLORS[0]) {
            return code.toUpperCase();
        } else {
            return code.toLowerCase();
        }
    };

    static translateFenPiece(piece, color){
        for(let i = 0; i < CODES.length; ++i) {
            if(CODES[i][0] === piece) {
                return FenTranslator.translateColor(CODES[i][1], color);
            }
        }
    };

    static translateFenWhitePiece(piece) {
        return FenTranslator.translateFenPiece(piece, COLORS[0]);
    }

    static translateFenBlackPiece(piece) {
        return FenTranslator.translateFenPiece(piece, COLORS[1]);
    }

    static updateFenAfterPromotion(currentPosition, selectedPromotion) {
        let fenSections = FenTranslator.splitFenSections(currentPosition);
        let positions = FenTranslator.getPositionsAsRowArray(fenSections[FEN_POSITIONS_SECTION]);

        let whitePromotion = positions[0].indexOf('P');
        if(whitePromotion !== -1) {
            positions[0] = positions[0].replace('P', FenTranslator.translateFenWhitePiece(selectedPromotion));
        } else {
            positions[7] = positions[7].replace('p', FenTranslator.translateFenBlackPiece(selectedPromotion))
        }

        fenSections[FEN_POSITIONS_SECTION] = positions.join(FEN_POSITION_DELIMITER);
        return fenSections.join(FEN_SECTION_DELIMITER);
    }
}
