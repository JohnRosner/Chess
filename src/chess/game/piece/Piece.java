package chess.game.piece;

import chess.game.Engine;
import chess.game.Position;

import java.util.ArrayList;

public abstract class Piece {

    protected Position position;
    protected Color color;
    protected boolean movedYet;

    //Must point to the same board that is used by Game
    protected Piece[][] board;

    public Piece(Position pos, Color col, Piece[][] board) {
        position = pos;
        color = col;
        movedYet = false;
        this.board = board;
    }

    protected Piece(Position pos, Color col, Piece[][] board, boolean moved) {
        position = pos;
        color = col;
        movedYet = moved;
        this.board = board;
    }

    public abstract Piece makeCopy(Piece[][] newBoard);

    public boolean hasMovedYet() {
        return movedYet;
    }

    protected Piece pieceAt(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }

    public Color getColor() { return color; }
    public Position getPosition() { return position; }
    public abstract PieceTypes getPieceType();
    public abstract ArrayList<Position> allowedMoves(Piece[][] board);

    public ArrayList<Position> legalMoves() {
        ArrayList<Position> moves = allowedMoves(board);
        moves.removeIf(this::putsKingInCheck);
        return moves;
    }

    public boolean needsPromotion() {
        return false;
    }

    public void updatePosition(Position pos) {
        position = pos;
    }

    //If destination is a legal move, updates the internal position and position within board
    public boolean move(Position destination) {
        if (canMoveLegally(destination)) {
            board[position.getRow()][position.getCol()] = null;
            board[destination.getRow()][destination.getCol()] = this;
            position = destination;
            movedYet = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean canMove(Position destination) {
        return allowedMoves(board).contains(destination);
    }

    public boolean canMoveLegally(Position destination) {
        return legalMoves().contains(destination);
    }

    public boolean illegalPosition(Position pos) {
        //illegal is outside board
        if (pos.getRow() < 0 || pos.getRow() > 7 || pos.getCol() < 0 || pos.getCol() > 7) {
            return true;
        } else {
            //illegal if moving on top of own team
            if (pieceAt(pos) != null) {
                if (pieceAt(pos).getColor() == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean putsKingInCheck(Position pos) {
        Piece[][] boardWithMove = Engine.getBoardCopy(board);
        boardWithMove[position.getRow()][position.getCol()].updatePosition(pos);
        boardWithMove[pos.getRow()][pos.getCol()] = boardWithMove[position.getRow()][position.getCol()];
        boardWithMove[position.getRow()][position.getCol()] = null;
        return Engine.isCheck(boardWithMove, color);
    }

    public enum PieceTypes {
        PAWN, ROOK, BISHOP, KNIGHT, QUEEN, KING
    }

    public enum Color {
        BLACK, WHITE;

        @Override
        public String toString() {
            return this == WHITE ? "White" : "Black";
        }
    }
}
