const PENDING = '...';
export default class PgnUtils {

    static appendMove(moves, move) {
        var lastLeg = moves.pop();

        if(lastLeg.white === PENDING) {
            lastLeg.white = move;
            lastLeg.black = PENDING;
            moves.append(lastLeg);
        } else {
            lastLeg.black = move;
            var newLeg = {moveNumber: lastLeg.moveNumber+1, white:PENDING, black:''};
            moves.push(lastLeg, newLeg);
        }
        return moves;
    }

    static isEmpty(str) {
        return (str.length === 0 || !str.trim());
    }

    static exportPgn(moves) {
        var data = "";
        moves.forEach(move => {
            data += (move.moveNumber + ". " + move.white + " " + move.black + " ")
        });
        return data;
    }
}
