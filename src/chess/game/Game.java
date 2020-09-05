package chess.game;

import chess.game.ai.AI;
import chess.game.ai.MiniMaxAI;
import chess.game.piece.Piece;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Game {

    private Engine engine;

    private PlayerType white, black;
    private AI whiteAI, blackAI;

    private boolean pieceSelected;
    private Position selectedPiece;

    private long lastMove;

    public Game(PlayerType white, PlayerType black) {
        this.white = white;
        this.black = black;
        pieceSelected = false;

        engine = new Engine();

        if (white == PlayerType.AI) {
            whiteAI = new MiniMaxAI(Piece.Color.WHITE, engine);
        }
        if (black == PlayerType.AI) {
            blackAI = new MiniMaxAI(Piece.Color.BLACK, engine);
        }

        //This is only to initialize the variable lastMove
        completedMove();
    }

    public void clickOnSquare(Position position) {
        if (pieceSelected) {
            engine.makeMove(selectedPiece, position);
            pieceSelected = false;
            completedMove();
        } else {
            if (engine.pieceAt(position) == null) {
                return;
            }
            if (engine.isWhiteTurn()) {
                if (engine.pieceAt(position).getColor() == Piece.Color.BLACK || white == PlayerType.AI) {
                    return;
                }
            } else {
                if (engine.pieceAt(position).getColor() == Piece.Color.WHITE || black == PlayerType.AI) {
                    return;
                }
            }
            pieceSelected = true;
            selectedPiece = position;
        }
    }

    public void moveWhiteAI() {
        if (white == PlayerType.AI) {
            if (engine.isWhiteTurn()) {
                engine.makeMove(whiteAI.makeMove());
                completedMove();
                if (engine.doesPawnNeedPromotion()) {
                    engine.promotePawn(whiteAI.promotePawn());
                }
            }
        }
    }

    public void moveBlackAI() {
        if (black == PlayerType.AI) {
            if (!engine.isWhiteTurn()) {
                engine.makeMove(blackAI.makeMove());
                completedMove();
                if (engine.doesPawnNeedPromotion()) {
                    engine.promotePawn(blackAI.promotePawn());
                }
            }
        }
    }

    private void completedMove() {
        lastMove = Calendar.getInstance().getTimeInMillis();
    }

    public long getLastMove() {
        return lastMove;
    }

    public boolean isCheckMate(Piece.Color color) {
        return engine.isCheckMate(color);
    }

    public boolean isStalemate() {
        return engine.isStalemate();
    }

    public boolean isWhiteTurn() {
        return engine.isWhiteTurn();
    }

    public ArrayList<Position> getSelectedPieceAttack() {
        return engine.pieceAt(selectedPiece).legalMoves();
    }

    public ArrayList<Position> getSelectedPieceIllegalAttack() {
        ArrayList<Position> moves = engine.pieceAt(selectedPiece).allowedMoves(engine.getBoard());
        ArrayList<Position> legalMoves = engine.pieceAt(selectedPiece).legalMoves();
        moves.removeAll(legalMoves);
        return moves;
    }

    public Piece pieceAt(int row, int col) {
        return engine.pieceAt(row, col);
    }

    public boolean doesPawnNeedPromotion() {
        return engine.doesPawnNeedPromotion();
    }

    public Piece.Color pawnColor() {
        return engine.pawnColor();
    }

    public void promotePawn(Piece.PieceTypes selection) {
        engine.promotePawn(selection);
    }

    public Map<Piece.PieceTypes, Integer> getWhiteDead() {
        return engine.getWhiteDead();
    }

    public Map<Piece.PieceTypes, Integer> getBlackDead() {
        return engine.getBlackDead();
    }

    public boolean isPieceSelected() {
        return pieceSelected;
    }

    public Position getSelectedPiece() {
        return pieceSelected ? selectedPiece : null;
    }

    public boolean hasPlayer() {
        return white == PlayerType.HUMAN || black == PlayerType.HUMAN;
    }

    public boolean hasAI() {
        return white != PlayerType.HUMAN || black != PlayerType.HUMAN;
    }

    public boolean isWhiteAI() {
        return white == PlayerType.AI;
    }

    public boolean isBlackAI() {
        return black == PlayerType.AI;
    }

    public enum PlayerType {
        AI, HUMAN;
    }

}
