package chess.game;

import chess.game.piece.Piece;

public class Move {
    private Position initial;
    private Position destination;
    private int value;

    public Move(Position initial, Position destination) {
        this.initial = initial;
        this.destination = destination;
    }

    public Position getInitial() {
        return initial;
    }

    public Position getDestination() {
        return destination;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isLegalMove(Piece[][] board) {
        return board[initial.getRow()][initial.getCol()].canMove(destination);
    }

    @Override
    public String toString() {
        return "Move from " + initial +
                " to " + destination +
                " has value " + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        return ((Move) obj).initial.equals(initial) && ((Move) obj).destination.equals(destination);
    }
}
