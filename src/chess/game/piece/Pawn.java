package chess.game.piece;

import chess.game.Position;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(Position pos, Color col, Piece[][] board) {
        super(pos, col, board);
    }
    private Pawn(Position pos, Color col, Piece[][] board, boolean moved) {
        super(pos, col, board, moved);
    }
    @Override
    public Piece makeCopy(Piece[][] newBoard) {
        return new Pawn(position, color, newBoard, movedYet);
    }

    @Override
    public Piece.PieceTypes getPieceType() {
        return PieceTypes.PAWN;
    }

    @Override
    public ArrayList<Position> allowedMoves(Piece[][] board) {
        ArrayList<Position> moves = new ArrayList<>();
        if ((color == Color.BLACK && position.getRow() == 7) || (color == Color.WHITE && position.getRow() == 0)) {
            return moves;
        }

        //Check if can move forward
        if (color == Color.BLACK && board[position.getRow() + 1][position.getCol()] == null) {
            moves.add((new Position(position.getRow() + 1, position.getCol())));
        }
        if (color == Color.WHITE && board[position.getRow() - 1][position.getCol()] == null) {
            moves.add((new Position(position.getRow() - 1, position.getCol())));
        }

        //Check if in original position and can move two spaces
        if (color == Color.WHITE && position.getRow() == 6) {
            if (board[5][position.getCol()] == null && board[4][position.getCol()] == null) {
                moves.add(new Position(4, position.getCol()));
            }
        }
        if (color == Color.BLACK && position.getRow() == 1) {
            if (board[2][position.getCol()] == null && board[3][position.getCol()] == null) {
                moves.add(new Position(3, position.getCol()));
            }
        }

        //Check if can attack a piece
        if (position.getCol() != 0) {
            if (color == Color.WHITE) {
                if (board[position.getRow() - 1][position.getCol() - 1] != null) {
                    if (board[position.getRow() - 1][position.getCol() - 1].getColor() == Color.BLACK) {
                        moves.add(new Position(position.getRow() - 1, position.getCol() - 1));
                    }
                }
            } else {
                if (board[position.getRow() + 1][position.getCol() - 1] != null) {
                    if (board[position.getRow() + 1][position.getCol() - 1].getColor() == Color.WHITE) {
                        moves.add(new Position(position.getRow() + 1, position.getCol() - 1));
                    }
                }
            }
        }
        if (position.getCol() != 7) {
            if (color == Color.WHITE) {
                if (board[position.getRow() - 1][position.getCol() + 1] != null) {
                    if (board[position.getRow() - 1][position.getCol() + 1].getColor() == Color.BLACK) {
                        moves.add(new Position(position.getRow() - 1, position.getCol() + 1));
                    }
                }
            } else {
                if (board[position.getRow() + 1][position.getCol() + 1] != null) {
                    if (board[position.getRow() + 1][position.getCol() + 1].getColor() == Color.WHITE) {
                        moves.add(new Position(position.getRow() + 1, position.getCol() + 1));
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public boolean needsPromotion() {
        return (color == Color.BLACK && position.getRow() == 7) || (color == Color.WHITE && position.getRow() == 0);
    }

}
