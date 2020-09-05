package chess.game.piece;

import chess.game.Position;

import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(Position pos, Color col, Piece[][] board) {
        super(pos, col, board);
    }
    private Knight(Position pos, Color col, Piece[][] board, boolean moved) {
        super(pos, col, board, moved);
    }
    @Override
    public Piece makeCopy(Piece[][] newBoard) {
        return new Knight(position, color, newBoard, movedYet);
    }

    @Override
    public PieceTypes getPieceType() {
        return PieceTypes.KNIGHT;
    }

    @Override
    public ArrayList<Position> allowedMoves(Piece[][] board) {
        ArrayList<Position> moves = new ArrayList<>();
        moves.add(new Position(position.getRow() + 2, position.getCol() + 1));
        moves.add(new Position(position.getRow() + 2, position.getCol() - 1));
        moves.add(new Position(position.getRow() - 2, position.getCol() + 1));
        moves.add(new Position(position.getRow() - 2, position.getCol() - 1));
        moves.add(new Position(position.getRow() + 1, position.getCol() + 2));
        moves.add(new Position(position.getRow() + 1, position.getCol() - 2));
        moves.add(new Position(position.getRow() - 1, position.getCol() + 2));
        moves.add(new Position(position.getRow() - 1, position.getCol() - 2));

        this.board = board;
        moves.removeIf(this::illegalPosition);

        return moves;
    }

}
