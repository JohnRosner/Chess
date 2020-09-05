package chess.game.piece;

import chess.game.Position;

import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(Position pos, Color col, Piece[][] board) {
        super(pos, col, board);
    }
    private Rook(Position pos, Color col, Piece[][] board, boolean moved) {
        super(pos, col, board, moved);
    }

    @Override
    public Piece makeCopy(Piece[][] newBoard) {
        return new Rook(position, color, newBoard, movedYet);
    }

    @Override
    public PieceTypes getPieceType() {
        return PieceTypes.ROOK;
    }


    @Override
    public ArrayList<Position> allowedMoves(Piece[][] board) {
        ArrayList<Position> moves = new ArrayList<>();

        int col = position.getCol() + 1;
        while (col < 8) {
            if (board[position.getRow()][col] != null) {
                if (board[position.getRow()][col].getColor() != color) {
                    moves.add(new Position(position.getRow(), col));
                }
                break;
            }
            moves.add(new Position(position.getRow(), col));
            col++;
        }

        col = position.getCol() - 1;
        while (col >= 0) {
            if (board[position.getRow()][col] != null) {
                if (board[position.getRow()][col].getColor() != color) {
                    moves.add(new Position(position.getRow(), col));
                }
                break;
            }
            moves.add(new Position(position.getRow(), col));
            col--;
        }

        int row = position.getRow() + 1;
        while (row < 8) {
            if (board[row][position.getCol()] != null) {
                if (board[row][position.getCol()].getColor() != color) {
                    moves.add(new Position(row, position.getCol()));
                }
                break;
            }
            moves.add(new Position(row, position.getCol()));
            row++;
        }

        row = position.getRow() - 1;
        while (row >= 0) {
            if (board[row][position.getCol()] != null) {
                if (board[row][position.getCol()].getColor() != color) {
                    moves.add(new Position(row, position.getCol()));
                }
                break;
            }
            moves.add(new Position(row, position.getCol()));
            row--;
        }


        return moves;
    }

}
