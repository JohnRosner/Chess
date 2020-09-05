package chess.game.ai;

import chess.game.Engine;
import chess.game.Move;
import chess.game.piece.Piece;

import java.util.ArrayList;
import java.util.LinkedList;

public class MiniMaxAI extends AI {

    static boolean printMoves = false;
    static boolean printBoardValue  = false;

    private final static int depth = 4;
    private int movesDone;

    private LinkedList<Move> previousMoves;

    public MiniMaxAI(Piece.Color col, Engine ga) {
        super(col, ga);
        movesDone = 0;
        previousMoves = new LinkedList<>();
    }

    @Override
    public Move makeMove() {
        movesDone++;
        if (printMoves) {
            System.out.println("This is move: " + movesDone);
        }
        Move bestMove = findBestMove(game.getBoard());
        previousMoves.add(bestMove);
        if (previousMoves.size() > 4) {
            previousMoves.removeFirst();
        }
        return bestMove;
    }

    @Override
    public Piece.PieceTypes promotePawn() {
        return Piece.PieceTypes.QUEEN;
    }

    private Move findBestMove(Piece[][] board) {
        return findBestMove(board, color,1, -100000, 100000);
    }

    private Move findBestMove(Piece[][] board, Piece.Color color, int currentDepth, int alpha, int beta) {
        ArrayList<Move> moves = getLegalMoves(board, color);

        if (moves.isEmpty()) {
            return null;
        }

        //TODO: add or condition to check for end of game conditions
        if (currentDepth == depth) {
            return findMaxValueMove(board, moves, color);
        }

        Move bestMove = moves.get(0);

        if (color == Piece.Color.WHITE) {
            for (int i = 0; i < moves.size(); i++) {
                Piece[][] boardWithMove = Engine.getBoardWithMove(board, moves.get(i));
                Move bestOpponentMove = findBestMove(boardWithMove, Piece.Color.BLACK, currentDepth + 1, alpha, beta);
                if (bestOpponentMove != null) {
                    moves.get(i).setValue(bestOpponentMove.getValue());
                } else {
                    moves.get(i).setValue(10000);
                }

                if (moves.get(i).getValue() > bestMove.getValue() && !previousMoves.contains(moves.get(i))) {
                    bestMove = moves.get(i);
                }

                if (moves.get(i).getValue() > alpha) {
                    alpha = moves.get(i).getValue();
                }
                if (beta <= alpha) {
                    break;
                }

            }
        } else {
            for (int i = 0; i < moves.size(); i++) {
                Piece[][] boardWithMove = Engine.getBoardWithMove(board, moves.get(i));
                Move bestOpponentMove = findBestMove(boardWithMove, Piece.Color.WHITE, currentDepth + 1, alpha, beta);
                if (bestOpponentMove != null) {
                    moves.get(i).setValue(bestOpponentMove.getValue());
                } else {
                    moves.get(i).setValue(-10000);
                }

                if (moves.get(i).getValue() < bestMove.getValue() && !previousMoves.contains(moves.get(i))) {
                    bestMove = moves.get(i);
                }

                if (moves.get(i).getValue() < beta) {
                    beta = moves.get(i).getValue();
                }
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return bestMove;


    }

    private Move findMaxValueMove(Piece[][] board, ArrayList<Move> moves, Piece.Color color) {
        for (int i = 0; i < moves.size(); i++) {
            moves.get(i).setValue(getValueOfBoard(Engine.getBoardWithMove(board, moves.get(i))));
        }
        int bestIndex = 0;
        for (int i = 0; i < moves.size(); i++) {
            if (color == Piece.Color.WHITE) {
                if (moves.get(i).getValue() > moves.get(bestIndex).getValue()) {
                    bestIndex = i;
                }
            } else {
                if (moves.get(i).getValue() < moves.get(bestIndex).getValue()) {
                    bestIndex = i;
                }
            }
        }
        return moves.get(bestIndex);
    }

    private int getValueOfBoard(Piece[][] board) {
        boolean isEndgame = isEndgame(board);
        int value = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    int pieceValue = Values.getValueOfPiece(board[i][j].getPieceType(), i, j, color, isEndgame);
                    value = board[i][j].getColor() == Piece.Color.WHITE ? value + pieceValue : value - pieceValue;
                }
            }
        }

        if (printBoardValue) {
            Engine.printBoard(board);
            System.out.println("Has value: " + value + '\n');
        }

        //Add value for putting opponent in check and decrease value if AI is in check
        if (Engine.isCheck(board, color == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE)) {
            value += 300;
        }
        if (Engine.isCheck(board, color)) {
            value -= 1000;
        }

        return value;
    }

    private boolean isEndgame(Piece[][] board) {
        int numberOfPieces = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    numberOfPieces++;
                }
            }
        }
        return numberOfPieces < 18;
    }

}
