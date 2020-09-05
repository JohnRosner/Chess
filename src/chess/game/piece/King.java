package chess.game.piece;

import chess.game.Position;

import java.util.ArrayList;

public class King extends Piece {

    public King(Position pos, Color col, Piece[][] board) {
        super(pos, col, board);
    }
    private King(Position pos, Color col, Piece[][] board, boolean moved) {
        super(pos, col, board, moved);
    }

    @Override
    public Piece makeCopy(Piece[][] newBoard) {
        return new King(position, color, newBoard, movedYet);
    }

    @Override
    public PieceTypes getPieceType() {
        return PieceTypes.KING;
    }

    @Override
    public boolean move(Position destination) {
        if (!movedYet) {
            if (color == Color.WHITE) {
                if (destination.getRow() == 7 && destination.getCol() == 2) {
                    return castle(destination);
                }
                if (destination.getRow() == 7 && destination.getCol() == 6) {
                    return castle(destination);
                }
            } else {
                if (destination.getRow() == 0 && destination.getCol() == 2) {
                    return castle(destination);
                }
                if (destination.getRow() == 0 && destination.getCol() == 6) {
                    return castle(destination);
                }
            }
        }
        return super.move(destination);
    }

    //king: the position the king is going to
    //requires that it is valid to castle
    //This method updates the board for the castling move, it updates the position of the rook
    private boolean castle(Position king) {
        int row = king.getRow();
        boolean kingside = king.getCol() == 6;

        board[row][kingside ? 5 : 3] = board[row][kingside ? 7 : 0];
        board[row][kingside ? 7 : 0] = null;
        board[row][kingside ? 5 : 3].updatePosition(new Position(row, kingside ? 5 : 3));

        board[position.getRow()][position.getCol()] = null;
        board[king.getRow()][king.getCol()] = this;
        updatePosition(king);

        movedYet = true;
        return true;
    }

    @Override
    public ArrayList<Position> allowedMoves(Piece[][] board) {
        ArrayList<Position> moves = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            moves.add(new Position(position.getRow() + 1, position.getCol() + i));
            moves.add(new Position(position.getRow() - 1, position.getCol() + i));
        }
        moves.add(new Position(position.getRow(), position.getCol() - 1));
        moves.add(new Position(position.getRow(), position.getCol() + 1));

        this.board = board;
        moves.removeIf(this::illegalPosition);

        //Deal with castling
        if (!movedYet) {
            if (color == Color.WHITE) {
                if (board[7][0] != null) {
                    if (board[7][0].getPieceType() == PieceTypes.ROOK && !board[7][0].hasMovedYet()) {
                        if (board[7][1] == null && board[7][2] == null && board[7][3] == null) {
                            moves.add(new Position(7, 2));
                        }
                    }
                }
                if (board[7][7] != null) {
                    if (board[7][7].getPieceType() == PieceTypes.ROOK && !board[7][7].hasMovedYet()) {
                        if (board[7][5] == null && board[7][6] == null) {
                            moves.add(new Position(7, 6));
                        }
                    }
                }
            } else {
                if (board[0][0] != null) {
                    if (board[0][0].getPieceType() == PieceTypes.ROOK && !board[0][0].hasMovedYet()) {
                        if (board[0][1] == null && board[0][2] == null && board[0][3] == null) {
                            moves.add(new Position(0, 2));
                        }
                    }
                }
                if (board[0][7] != null) {
                    if (board[0][7].getPieceType() == PieceTypes.ROOK && !board[0][7].hasMovedYet()) {
                        if (board[0][5] == null && board[0][6] == null) {
                            moves.add(new Position(0, 6));
                        }
                    }
                }
            }
        }

        return moves;
    }

}
