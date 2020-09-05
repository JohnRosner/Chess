package chess.game.ai;

import chess.game.Engine;
import chess.game.Move;
import chess.game.piece.Piece;
import chess.game.Position;

import java.util.ArrayList;

public abstract class AI {

    protected Piece.Color color;
    protected Engine game;

    public AI(Piece.Color col, Engine ga) {
        color = col;
        game = ga;
    }

    public Piece.Color getColor() {
        return color;
    }

    public abstract Move makeMove();

    public abstract Piece.PieceTypes promotePawn();

    protected ArrayList<Move> getLegalMoves() {
        return getLegalMoves(game.getBoard(), this.color);
    }

    protected ArrayList<Move> getLegalMoves(Piece[][] board, Piece.Color color) {
        ArrayList<Piece> side = getPieces(board, color);
        ArrayList<Move> legalMoves = new ArrayList<>();

        for (Piece piece : side) {
            ArrayList<Position> destination = piece.legalMoves();
            Position initial = piece.getPosition();
            for (Position dest : destination) {
                legalMoves.add(new Move(initial, dest));
            }
        }

        return legalMoves;
    }

    protected ArrayList<Piece> getPieces(Piece[][] board, Piece.Color color) {
        ArrayList<Piece> side = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].getColor() == color) {
                        side.add(board[i][j]);
                    }
                }
            }
        }
        return side;
    }

}
