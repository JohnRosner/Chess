package chess.game.piece;

import chess.game.Position;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(Position pos, Color col, Piece[][] board) {
        super(pos, col, board);
    }
    private Queen(Position pos, Color col, Piece[][] board, boolean moved) {
        super(pos, col, board, moved);
    }

    @Override
    public Piece makeCopy(Piece[][] newBoard) {
        return new Queen(position, color, newBoard, movedYet);
    }

    @Override
    public PieceTypes getPieceType() {
        return PieceTypes.QUEEN;
    }

    @Override
    public ArrayList<Position> allowedMoves(Piece[][] board) {
        //Create a rook and bishop at the queen's position, if the bishop or rook can move to a square, so can a queen.
        Rook dummyRook = new Rook(position, color, board);
        Bishop dummyBishop = new Bishop(position, color, board);
        ArrayList<Position> moves = new ArrayList<>(dummyRook.allowedMoves(board));
        moves.addAll(dummyBishop.allowedMoves(board));
        return moves;
    }

}
