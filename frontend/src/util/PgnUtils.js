const PENDING = '...';
export default class PgnUtils {

    static appendMove(moves, move) {
        var lastLeg = moves.pop();
        if(lastLeg.white === PENDING) {
            lastLeg.white = move;
            lastLeg.black = PENDING;
            moves.push(lastLeg);
        } else if(lastLeg.black === PENDING){
            lastLeg.black = move;
            var newLeg = {moveNumber: lastLeg.moveNumber+1, white:PENDING, black:''};
            moves.push(lastLeg, newLeg);
        } else {
            moves.push(lastLeg);
        }
        return moves;
    }

    static isEmpty(str) {
        return (str.length === 0 || !str.trim());
    }

    static exportPgn(moves) {
        var data =
            "[Event \"\"] \n" +
            "[Site \"\"] \n" +
            "[Date \"\"] \n" +
            "[Round \"\"] \n" +
            "[White \"\"] \n" +
            "[Black \"\"] \n" +
            "[Result \"\"] \n";
        moves.forEach(move => {
            data += (move.moveNumber + ". " + move.white + " " + move.black + " ")
        });
        return data;
    }
}
