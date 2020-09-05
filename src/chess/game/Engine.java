package chess.game;

import chess.game.piece.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Engine {

    private Piece[][] board;
    private boolean whiteTurn;
    private boolean pawnNeedsPromotion;
    private Position pawn;

    private ArrayList<Piece> whiteDead;
    private ArrayList<Piece> blackDead;

    public Engine() {
        whiteTurn = true;
        pawnNeedsPromotion = false;
        whiteDead = new ArrayList<>();
        blackDead = new ArrayList<>();
        board = getResetBoard();
    }

    private Piece[][] getResetBoard() {
        Piece[][] board = new Piece[8][8];
        board[0][0] = new Rook(new Position(0,0), Piece.Color.BLACK, board);
        board[0][7] = new Rook(new Position(0,7), Piece.Color.BLACK, board);
        board[0][1] = new Knight(new Position(0,1), Piece.Color.BLACK, board);
        board[0][6] = new Knight(new Position(0,6), Piece.Color.BLACK, board);
        board[0][2] = new Bishop(new Position(0,2), Piece.Color.BLACK, board);
        board[0][5] = new Bishop(new Position(0,5), Piece.Color.BLACK, board);
        board[0][3] = new Queen(new Position(0, 3), Piece.Color.BLACK, board);
        board[0][4] = new King(new Position(0, 4), Piece.Color.BLACK, board);

        board[7][0] = new Rook(new Position(7,0), Piece.Color.WHITE, board);
        board[7][7] = new Rook(new Position(7,7), Piece.Color.WHITE, board);
        board[7][1] = new Knight(new Position(7,1), Piece.Color.WHITE, board);
        board[7][6] = new Knight(new Position(7,6), Piece.Color.WHITE, board);
        board[7][2] = new Bishop(new Position(7,2), Piece.Color.WHITE, board);
        board[7][5] = new Bishop(new Position(7,5), Piece.Color.WHITE, board);
        board[7][3] = new Queen(new Position(7, 3), Piece.Color.WHITE, board);
        board[7][4] = new King(new Position(7, 4), Piece.Color.WHITE, board);

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(new Position(1, i), Piece.Color.BLACK, board);
            board[6][i] = new Pawn(new Position(6, i), Piece.Color.WHITE, board);
        }

        return board;
    }

    public boolean makeMove(Position initial, Position destination) {
        Piece destinationPiece = pieceAt(destination);
        if (pieceAt(initial).move(destination)) {
            if (destinationPiece != null) {
                if (destinationPiece.getColor() == Piece.Color.WHITE)
                    whiteDead.add(destinationPiece);
                else
                    blackDead.add(destinationPiece);
            }

            whiteTurn = !whiteTurn;

            if (pieceAt(destination).needsPromotion()) {
                pawnNeedsPromotion = true;
                pawn = destination;
            } else {
                pawnNeedsPromotion = false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean makeMove(Move move) {
        return makeMove(move.getInitial(), move.getDestination());
    }

    public Piece[][] getBoard() {
        return board;
    }

    public static void printBoard(Piece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    System.out.print("* ");
                } else {
                    switch(board[i][j].getPieceType()) {
                        case QUEEN:
                            System.out.print(board[i][j].getColor() == Piece.Color.WHITE ? "Q " : "q ");
                            break;
                        case KNIGHT:
                            System.out.print(board[i][j].getColor() == Piece.Color.WHITE ? "H " : "h ");
                            break;
                        case KING:
                            System.out.print(board[i][j].getColor() == Piece.Color.WHITE ? "K " : "k ");
                            break;
                        case ROOK:
                            System.out.print(board[i][j].getColor() == Piece.Color.WHITE ? "R " : "r ");
                            break;
                        case BISHOP:
                            System.out.print(board[i][j].getColor() == Piece.Color.WHITE ? "B ": "b ");
                            break;
                        case PAWN:
                            System.out.print(board[i][j].getColor() == Piece.Color.WHITE ? "P " : "p ");
                            break;
                    }
                }
            }
            System.out.println();
        }
    }

    public static Piece[][] getBoardWithMove(Piece[][] board, Move move) {
        Piece[][] newBoard = getBoardCopy(board);
        newBoard[move.getInitial().getRow()][move.getInitial().getCol()].move(move.getDestination());
        return newBoard;
    }

    public static Piece[][] getBoardCopy(Piece[][] board) {
        Piece[][] newBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard[i][j] = board[i][j] == null ? null : board[i][j].makeCopy(newBoard);
            }
        }
        return newBoard;
    }

    //Returns true if the king of Color is in check, false otherwise
    //Static so that the pieces can use it without having an instance of game and so they can use it on a hypothetical board
    public static boolean isCheck(Piece[][] board, Piece.Color color) {
        Position king = getKingPosition(board, color);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].getColor() != color) {
                        if (board[i][j].canMove(king)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static Position getKingPosition(Piece[][] board, Piece.Color color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].getPieceType() == Piece.PieceTypes.KING && board[i][j].getColor() == color) {
                        return new Position(i, j);
                    }
                }
            }
        }
        System.out.println("Can't find king");
        return new Position(-1, -1);
    }

    public boolean isCheckMate(Piece.Color color) {
        boolean canMove = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j].getColor() == color && board[i][j].legalMoves().size() > 0) {
                        canMove = true;
                    }
                }
            }
        }
        return !canMove && isCheck(board, color);
    }

    public boolean isStalemate() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (whiteTurn ? board[i][j].getColor() == Piece.Color.WHITE : board[i][j].getColor() == Piece.Color.BLACK) {
                        if (!board[i][j].legalMoves().isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean doesPawnNeedPromotion() {
        return pawnNeedsPromotion;
    }

    public Piece.Color pawnColor() {
        return pieceAt(pawn).getColor();
    }

    public void promotePawn(Piece.PieceTypes selection) {
        Piece.Color pawnColor = pieceAt(pawn).getColor();
        switch (selection) {
            case QUEEN:
                board[pawn.getRow()][pawn.getCol()] = new Queen(pawn, pawnColor, board);
                break;
            case ROOK:
                board[pawn.getRow()][pawn.getCol()] = new Rook(pawn, pawnColor, board);
                break;
            case BISHOP:
                board[pawn.getRow()][pawn.getCol()] = new Bishop(pawn, pawnColor, board);
                break;
            case KNIGHT:
                board[pawn.getRow()][pawn.getCol()] = new Knight(pawn, pawnColor, board);
                break;
        }
        pawnNeedsPromotion = false;
    }

    public Piece pieceAt(int row, int col) {
        return board[row][col];
    }

    public Piece pieceAt(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public Map<Piece.PieceTypes, Integer> getWhiteDead() {
        return getMap(whiteDead);
    }

    public Map<Piece.PieceTypes, Integer> getBlackDead() {
        return getMap(blackDead);
    }

    private Map<Piece.PieceTypes, Integer> getMap(ArrayList<Piece> arr) {
        Map<Piece.PieceTypes, Integer> map = new HashMap<>();
        map.put(Piece.PieceTypes.PAWN, 0);
        map.put(Piece.PieceTypes.ROOK, 0);
        map.put(Piece.PieceTypes.BISHOP, 0);
        map.put(Piece.PieceTypes.KNIGHT, 0);
        map.put(Piece.PieceTypes.QUEEN, 0);
        for (Piece piece : arr) {
            switch(piece.getPieceType()) {
                case PAWN:
                    map.put(Piece.PieceTypes.PAWN, map.get(Piece.PieceTypes.PAWN) + 1);
                    break;
                case ROOK:
                    map.put(Piece.PieceTypes.ROOK, map.get(Piece.PieceTypes.ROOK) + 1);
                    break;
                case BISHOP:
                    map.put(Piece.PieceTypes.BISHOP, map.get(Piece.PieceTypes.BISHOP) + 1);
                    break;
                case KNIGHT:
                    map.put(Piece.PieceTypes.KNIGHT, map.get(Piece.PieceTypes.KNIGHT) + 1);
                    break;
                case QUEEN:
                    map.put(Piece.PieceTypes.QUEEN, map.get(Piece.PieceTypes.QUEEN) + 1);
                    break;
            }
        }
        return map;
    }

}
