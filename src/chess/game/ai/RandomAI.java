package chess.game.ai;

import chess.game.Engine;
import chess.game.Move;
import chess.game.piece.Piece;

import java.util.ArrayList;

public class RandomAI extends AI {

    public RandomAI(Piece.Color col, Engine ga) {
        super(col, ga);
    }

    @Override
    public Move makeMove() {
        ArrayList<Move> legalMoves = getLegalMoves();
        int randomMove = (int) (Math.random() * legalMoves.size());
        return legalMoves.get(randomMove);
    }

    @Override
    public Piece.PieceTypes promotePawn() {
        return Piece.PieceTypes.QUEEN;
    }


}
