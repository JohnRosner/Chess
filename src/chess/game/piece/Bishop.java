package chess.game.piece;

import chess.game.Position;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(Position pos, Color col, Piece[][] board) {
        super(pos, col, board);
    }
    private Bishop(Position pos, Color col, Piece[][] board, boolean moved) {
        super(pos, col, board, moved);
    }

    @Override
    public Piece makeCopy(Piece[][] newBoard) {
        return new Bishop(position, color, newBoard, movedYet);
    }

    @Override
    public PieceTypes getPieceType() {
        return PieceTypes.BISHOP;
    }

    @Override
    public ArrayList<Position> allowedMoves(Piece[][] board) {
        ArrayList<Position> moves = new ArrayList<>();

        int col = position.getCol() + 1;
        int row = position.getRow() + 1;
        while (col < 8 && row < 8) {
            if (board[row][col] != null) {
                if (board[row][col].getColor() != color) {
                    moves.add(new Position(row, col));
                }
                break;
            }
            moves.add(new Position(row, col));
            col++;
            row++;
        }

        col = position.getCol() - 1;
        row = position.getRow() + 1;
        while (col >= 0 && row < 8) {
            if (board[row][col] != null) {
                if (board[row][col].getColor() != color) {
                    moves.add(new Position(row, col));
                }
                break;
            }
            moves.add(new Position(row, col));
            col--;
            row++;
        }

        col = position.getCol() + 1;
        row = position.getRow() - 1;
        while (col < 8 && row >= 0) {
            if (board[row][col] != null) {
                if (board[row][col].getColor() != color) {
                    moves.add(new Position(row, col));
                }
                break;
            }
            moves.add(new Position(row, col));
            col++;
            row--;
        }

        col = position.getCol() - 1;
        row = position.getRow() - 1;
        while (col >= 0 && row >= 0) {
            if (board[row][col] != null) {
                if (board[row][col].getColor() != color) {
                    moves.add(new Position(row, col));
                }
                break;
            }
            moves.add(new Position(row, col));
            col--;
            row--;
        }


        return moves;
    }
}
