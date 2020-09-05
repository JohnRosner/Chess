package chess.game.ai;

import chess.game.piece.Piece;

public class Values {

    private static final int[][] PawnPositionValue =
            {{0,  0,   0,   0,   0,   0,  0, 0},
            {50, 50,  50,  50,  50,  50, 50, 50},
            {10, 10,  20,  30,  30,  20, 10, 10},
            { 5,  5,  10,  25,  25,  10,  5, 5},
            { 0,  0,   0,  20,  20,   0,  0, 0},
            { 5, -5, -10,   0,   0, -10, -5, 5},
            { 5, 10,  10, -20, -20,  10, 10, 5},
            { 0,  0,   0,   0,   0,   0,  0, 0}};

    private static final int[][] KnightPositionValue =
            {{-50, -40, -30, -30, -30, -30, -40, -50},
            {-40,  -20,   0,   0,   0,   0, -20, -40},
            {-30,    0,  10,  15,  15,  10,   0, -30},
            {-30,    5,  15,  20,  20,  15,   5, -30},
            {-30,    0,  15,  20,  20,  15,   0, -30},
            {-30,    5,  10,  15,  15,  10,   5, -30},
            {-40,  -20,   0,   5,   5,   0, -20, -40},
            {-50,  -40, -30, -30, -30, -30, -40, -50}};

    private static final int[][] BishopPositionValue =
            {{-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}};

    private static final int[][] RookPositionValue =
            {{ 0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {0,  0,  0,  5,  5,  0,  0,  0}};

    private static final int[][] QueenPositionValue =
            {{-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}};

    private static final int[][] KingPositionValueMiddleGame =
            {{-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 10,  0,  0, 10, 30, 20}};

    private static final int[][] KingPositionValueEndGame =
            {{-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}};

    public static int getValueOfPiece(Piece.PieceTypes pieceType, int row, int col, Piece.Color color, boolean endgame) {
        int value = 0;
        switch (pieceType) {
            case PAWN:
                value += PieceValue.PAWN.getValue();
                value += color == Piece.Color.WHITE ? PawnPositionValue[row][col] : PawnPositionValue[7 - row][col];
                break;
            case BISHOP:
                value += PieceValue.BISHOP.getValue();
                value += color == Piece.Color.WHITE ? BishopPositionValue[row][col] : BishopPositionValue[7 - row][col];
                break;
            case KNIGHT:
                value += PieceValue.KNIGHT.getValue();
                value += color == Piece.Color.WHITE ? KnightPositionValue[row][col] : KnightPositionValue[7 - row][col];
                break;
            case ROOK:
                value += PieceValue.ROOK.getValue();
                value += color == Piece.Color.WHITE ? RookPositionValue[row][col] : KnightPositionValue[7 - row][col];
                break;
            case QUEEN:
                value += PieceValue.QUEEN.getValue();
                value += color == Piece.Color.WHITE ? QueenPositionValue[row][col] : QueenPositionValue[7 - row][col];
                break;
            case KING:
                value += PieceValue.KING.getValue();
                if (endgame) {
                    value += color == Piece.Color.WHITE ? KingPositionValueEndGame[row][col] : KingPositionValueEndGame[7 - row][col];
                } else {
                    value += color == Piece.Color.WHITE ? KingPositionValueMiddleGame[row][col] : KingPositionValueMiddleGame[7 - row][col];
                }
                break;
        }

        return value;
    }


    private enum PieceValue {
        PAWN(100), KNIGHT(320), BISHOP(330), ROOK(500), QUEEN(900), KING(20000);

        int value;

        PieceValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
