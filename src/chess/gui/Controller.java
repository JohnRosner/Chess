package chess.gui;

import chess.Main;
import chess.game.Game;
import chess.game.piece.Piece;
import chess.game.Position;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class Controller {

    private int pieceWidth;
    private final int SecondsForAIToMove = 1;

    private Game game;

    private final Image whitePawn = new Image("images/White/WhitePawn.png");
    private final Image whiteBishop = new Image("images/White/WhiteBishop.png");
    private final Image whiteKing = new Image("images/White/WhiteKing.png");
    private final Image whiteKnight = new Image("images/White/WhiteKnight.png");
    private final Image whiteQueen = new Image("images/White/WhiteQueen.png");
    private final Image whiteRook = new Image("images/White/WhiteRook.png");

    private final Image blackPawn = new Image("images/Black/BlackPawn.png");
    private final Image blackBishop = new Image("images/Black/BlackBishop.png");
    private final Image blackKing = new Image("images/Black/BlackKing.png");
    private final Image blackKnight = new Image("images/Black/BlackKnight.png");
    private final Image blackQueen = new Image("images/Black/BlackQueen.png");
    private final Image blackRook = new Image("images/Black/BlackRook.png");

    private final Image boardImage = new Image("images/Board.png");
    private final Image attacked = new Image("images/Attacked.png");
    private final Image illegalAttack = new Image("images/IllegalAttack.png");
    private final Image selected = new Image("images/Selected.png");
    private final Image empty = new Image("images/Empty.png");
    private final Image outline = new Image("images/Outline.png");

    private Group[][] images;
    private ImageView[][] deadPieceImages;

    @FXML
    private GridPane gameGridPane, rightGridPane, deadPieceDisplay;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label gameLabel;

    @FXML
    private ImageView boardImageView, deadPieceOutline;

    private long lastPlayerMove;
    Timeline aiTimeline;


    public void initialize() {
        images = new Group[8][8];
        boardImageView.setImage(boardImage);
        boardImageView.setVisible(false);
        deadPieceOutline.setImage(outline);
        gameGridPane.alignmentProperty().setValue(Pos.TOP_CENTER);
        rightGridPane.alignmentProperty().setValue(Pos.TOP_CENTER);
        lastPlayerMove = Calendar.getInstance().getTimeInMillis();

        borderPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                images[i][j] = new Group();
                images[i][j].setAutoSizeChildren(true);
                images[i][j].minHeight(100);
                images[i][j].minWidth(100);
                images[i][j].setPickOnBounds(true);
                gameGridPane.add(images[i][j], j, i); // Yes I know its put as j, i into the GridPane, that's cause the GridPane is column, row but everything else is row, column

                final Position pos = new Position(i, j);
                images[i][j].setOnMousePressed(event -> {
                    //System.out.println("Mouse Clicked at " + pos.getRow() + ", " + pos.getCol());
                    game.clickOnSquare(pos);
                    updateBoard();
                    if (game.doesPawnNeedPromotion()) {
                        promotePawn(game.pawnColor());
                    }
                    updateDeadDisplay();
                    checkForWin();

                    event.consume();
                });

            }
        }

        aiTimeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            if (Calendar.getInstance().getTimeInMillis() - game.getLastMove() > SecondsForAIToMove * 1000) {
                moveAI();
            }
        }));
        aiTimeline.setCycleCount(Animation.INDEFINITE);

        setupDeadDisplay();

        displayMenu();
    }

    public void moveAI() {
        if (!game.hasAI()) {
            return;
        }
        if (game.isWhiteTurn()) {
            if (game.isWhiteAI()) {
                game.moveWhiteAI();
            } else {
                return;
            }
        } else {
            if (game.isBlackAI()) {
                game.moveBlackAI();
            } else {
                return;
            }
        }

        updateBoard();
        updateDeadDisplay();
        checkForWin();
    }


    public void setupImages() {
        pieceWidth = Main.height / 8;
        boardImageView.setFitWidth(8 * pieceWidth);
        boardImageView.setFitHeight(8 * pieceWidth);
        boardImageView.setVisible(true);

    }

    public void setupDeadDisplay() {
        deadPieceImages = new ImageView[4][8];

        for (int i = 0; i < 8; i++) {
            deadPieceImages[2][i] = new ImageView(whitePawn);
            deadPieceImages[1][i] = new ImageView(blackPawn);
        }

        deadPieceImages[0][0] = new ImageView(blackKnight);
        deadPieceImages[0][1] = new ImageView(blackKnight);
        deadPieceImages[0][2] = new ImageView(blackBishop);
        deadPieceImages[0][3] = new ImageView(blackBishop);
        deadPieceImages[0][4] = new ImageView(blackRook);
        deadPieceImages[0][5] = new ImageView(blackRook);
        deadPieceImages[0][6] = new ImageView(blackQueen);
        deadPieceImages[0][7] = new ImageView(blackQueen);

        deadPieceImages[3][0] = new ImageView(whiteKnight);
        deadPieceImages[3][1] = new ImageView(whiteKnight);
        deadPieceImages[3][2] = new ImageView(whiteBishop);
        deadPieceImages[3][3] = new ImageView(whiteBishop);
        deadPieceImages[3][4] = new ImageView(whiteRook);
        deadPieceImages[3][5] = new ImageView(whiteRook);
        deadPieceImages[3][6] = new ImageView(whiteQueen);
        deadPieceImages[3][7] = new ImageView(whiteQueen);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                deadPieceImages[i][j].setVisible(false);
                deadPieceImages[i][j].setFitHeight(35);
                deadPieceImages[i][j].setFitWidth(35);
                deadPieceDisplay.add(deadPieceImages[i][j], j + 1, i + 1);

            }
        }
    }

    public void updateDeadDisplay() {
        Map<Piece.PieceTypes, Integer> white = game.getWhiteDead();
        Map<Piece.PieceTypes, Integer> black = game.getBlackDead();

        switch(white.get(Piece.PieceTypes.PAWN)) {
            case 1:
                deadPieceImages[2][0].setVisible(true);
                break;
            case 2:
                deadPieceImages[2][1].setVisible(true);
                break;
            case 3:
                deadPieceImages[2][2].setVisible(true);
                break;
            case 4:
                deadPieceImages[2][3].setVisible(true);
                break;
            case 5:
                deadPieceImages[2][4].setVisible(true);
                break;
            case 6:
                deadPieceImages[2][5].setVisible(true);
                break;
            case 7:
                deadPieceImages[2][6].setVisible(true);
                break;
            case 8:
                deadPieceImages[2][7].setVisible(true);
                break;
        }

        switch(black.get(Piece.PieceTypes.PAWN)) {
            case 1:
                deadPieceImages[1][0].setVisible(true);
                break;
            case 2:
                deadPieceImages[1][1].setVisible(true);
                break;
            case 3:
                deadPieceImages[1][2].setVisible(true);
                break;
            case 4:
                deadPieceImages[1][3].setVisible(true);
                break;
            case 5:
                deadPieceImages[1][4].setVisible(true);
                break;
            case 6:
                deadPieceImages[1][5].setVisible(true);
                break;
            case 7:
                deadPieceImages[1][6].setVisible(true);
                break;
            case 8:
                deadPieceImages[1][7].setVisible(true);
                break;
        }

        if (white.get(Piece.PieceTypes.KNIGHT) > 0)
            deadPieceImages[3][0].setVisible(true);
        if (white.get(Piece.PieceTypes.KNIGHT) > 1)
            deadPieceImages[3][1].setVisible(true);

        if (white.get(Piece.PieceTypes.BISHOP) > 0)
            deadPieceImages[3][2].setVisible(true);
        if (white.get(Piece.PieceTypes.BISHOP) > 1)
            deadPieceImages[3][3].setVisible(true);

        if (white.get(Piece.PieceTypes.ROOK) > 0)
            deadPieceImages[3][4].setVisible(true);
        if (white.get(Piece.PieceTypes.ROOK) > 1)
            deadPieceImages[3][5].setVisible(true);

        if (white.get(Piece.PieceTypes.QUEEN) > 0)
            deadPieceImages[3][6].setVisible(true);
        if (white.get(Piece.PieceTypes.QUEEN) > 1)
            deadPieceImages[3][7].setVisible(true);

        if (black.get(Piece.PieceTypes.KNIGHT) > 0)
            deadPieceImages[0][0].setVisible(true);
        if (black.get(Piece.PieceTypes.KNIGHT) > 1)
            deadPieceImages[0][1].setVisible(true);

        if (black.get(Piece.PieceTypes.BISHOP) > 0)
            deadPieceImages[0][2].setVisible(true);
        if (black.get(Piece.PieceTypes.BISHOP) > 1)
            deadPieceImages[0][3].setVisible(true);

        if (black.get(Piece.PieceTypes.ROOK) > 0)
            deadPieceImages[0][4].setVisible(true);
        if (black.get(Piece.PieceTypes.ROOK) > 1)
            deadPieceImages[0][5].setVisible(true);

        if (black.get(Piece.PieceTypes.QUEEN) > 0)
            deadPieceImages[0][6].setVisible(true);
        if (black.get(Piece.PieceTypes.QUEEN) > 1)
            deadPieceImages[0][7].setVisible(true);

    }

    @FXML
    public void displayMenu() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getIcons().add(whitePawn);
        dialog.setAlwaysOnTop(true);
        dialog.setTitle("New Game");
        GridPane grid = new GridPane();
        BorderPane border = new BorderPane();
        grid.setHgap(25);
        grid.setVgap(15);
        grid.alignmentProperty().setValue(Pos.TOP_CENTER);
        Label label = new Label("Start a New Game");
        label.setFont(new Font("Ariel", 40));
        grid.add(label, 1, 0, 3, 1);

        border.setCenter(grid);
        border.setTop(label);
        border.setAlignment(label, Pos.CENTER);
        border.setAlignment(grid, Pos.CENTER);

        Label whiteLabel = new Label("White");
        whiteLabel.setFont(new Font("Ariel", 20));
        whiteLabel.setAlignment(Pos.CENTER);
        grid.add(whiteLabel, 0, 1);

        Label blackLabel = new Label("Black");
        blackLabel.setFont(new Font("Ariel", 20));
        blackLabel.setAlignment(Pos.CENTER);
        grid.add(blackLabel, 2, 1);

        ComboBox whiteComboBox = new ComboBox();
        whiteComboBox.getItems().addAll("Computer", "Human");
        grid.add(whiteComboBox, 0, 2);

        ComboBox blackComboBox = new ComboBox();
        blackComboBox.getItems().addAll("Computer", "Human");
        grid.add(blackComboBox, 2, 2);

        Button startGame = new Button("Start Game");
        startGame.setOnAction(actionEvent -> {
            Game.PlayerType white;
            Game.PlayerType black;
            switch ((String) whiteComboBox.getValue()) {
                case "Computer":
                    white = Game.PlayerType.AI;
                    break;
                case "Human":
                    white = Game.PlayerType.HUMAN;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + (String) whiteComboBox.getValue());
            }

            switch ((String) blackComboBox.getValue()) {
                case "Computer":
                    black = Game.PlayerType.AI;
                    break;
                case "Human":
                    black = Game.PlayerType.HUMAN;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + (String) blackComboBox.getValue());
            }
            newGame(white, black);
            dialog.close();
        });
        startGame.setAlignment(Pos.CENTER);
        startGame.setFont(new Font("Ariel", 20));
        grid.add(startGame, 0, 4, 3, 1);

        Scene dialogScene = new Scene(border, 500, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void newGame(Game.PlayerType white, Game.PlayerType black) {
        game = new Game(white, black);
        if (game.hasAI()) {
            aiTimeline.play();
        }

        String gameLabelString = "";
        gameLabelString += white == Game.PlayerType.HUMAN ? "Player " : "Computer ";
        gameLabelString += "vs ";
        gameLabelString += black == Game.PlayerType.HUMAN ? "Player" : "Computer";
        gameLabel.setText(gameLabelString);

        setupImages();
        updateBoard();
        updateDeadDisplay();

    }

    public void checkForWin() {
        if (game.isCheckMate(Piece.Color.WHITE)) {
            declareVictory(-1);
        } else if (game.isCheckMate(Piece.Color.BLACK)) {
            declareVictory(1);
        } else if (game.isStalemate()) {
            declareVictory(0);
        }
    }

    public void declareVictory(int side) {
        if (!game.hasPlayer()) {
            aiTimeline.stop();
        }
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getIcons().add(side == 1 ? whitePawn : blackPawn);
        dialog.setTitle(side != 0 ? "Victory" : "Stalemate");
        BorderPane border = new BorderPane();

        Label label = new Label(side == 0 ? "Tie!" : (side == 1 ? "White Wins!" : "Black Wins!"));
        label.setFont(new Font("Ariel", 50));
        border.setTop(label);
        border.setAlignment(label, Pos.CENTER);

        Button newGame = new Button("New Game");
        newGame.setOnAction(actionEvent -> {displayMenu(); dialog.close();});
        newGame.setFont(new Font("Ariel", 20));
        border.setCenter(newGame);
        border.setAlignment(newGame, Pos.TOP_CENTER);

        Scene dialogScene = new Scene(border, 400, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void promotePawn(Piece.Color color) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getIcons().add(color == Piece.Color.WHITE ? whitePawn : blackPawn);
        dialog.setTitle("Promote Pawn");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.alignmentProperty().setValue(Pos.TOP_CENTER);
        Label question = new Label("What would you like to promote your pawn to?");
        question.setFont(new Font("Ariel", 20));
        grid.add(question, 0, 0, 4, 1);

        ImageView queen = new ImageView(color == Piece.Color.WHITE ? whiteQueen : blackQueen);
        queen.setOnMouseClicked(actionEvent -> { game.promotePawn(Piece.PieceTypes.QUEEN); dialog.close(); updateBoard(); checkForWin();});
        ImageView rook = new ImageView(color == Piece.Color.WHITE ? whiteRook : blackRook);
        rook.setOnMouseClicked(actionEvent -> { game.promotePawn(Piece.PieceTypes.ROOK); dialog.close(); updateBoard(); checkForWin();});
        ImageView bishop = new ImageView(color == Piece.Color.WHITE ? whiteBishop : blackBishop);
        bishop.setOnMouseClicked(actionEvent -> { game.promotePawn(Piece.PieceTypes.BISHOP); dialog.close(); updateBoard(); checkForWin();});
        ImageView knight = new ImageView(color == Piece.Color.WHITE ? whiteKnight : blackKnight);
        knight.setOnMouseClicked(actionEvent -> { game.promotePawn(Piece.PieceTypes.KNIGHT); dialog.close(); updateBoard(); checkForWin();});

        grid.add(queen, 0, 1);
        grid.add(rook, 1, 1);
        grid.add(bishop, 2, 1);
        grid.add(knight, 3, 1);

        Scene dialogScene = new Scene(grid, 500, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void updateBoard() {

        ArrayList<Position> attackedSquares = null;
        ArrayList<Position> illegalMoves = null;
        if (game.isPieceSelected()) {
            attackedSquares = game.getSelectedPieceAttack();
            illegalMoves = game.getSelectedPieceIllegalAttack();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = game.pieceAt(i, j);
                images[i][j].getChildren().setAll();

                if (piece != null) {
                    switch (piece.getPieceType()) {
                        case PAWN:
                            images[i][j].getChildren().add(piece.getColor() == Piece.Color.WHITE ? createImageView(whitePawn) : createImageView(blackPawn));
                            break;
                        case BISHOP:
                            images[i][j].getChildren().add(piece.getColor() == Piece.Color.WHITE ? createImageView(whiteBishop) : createImageView(blackBishop));
                            break;
                        case ROOK:
                            images[i][j].getChildren().add(piece.getColor() == Piece.Color.WHITE ? createImageView(whiteRook) : createImageView(blackRook));
                            break;
                        case KNIGHT:
                            images[i][j].getChildren().add(piece.getColor() == Piece.Color.WHITE ? createImageView(whiteKnight) : createImageView(blackKnight));
                            break;
                        case QUEEN:
                            images[i][j].getChildren().add(piece.getColor() == Piece.Color.WHITE ? createImageView(whiteQueen) : createImageView(blackQueen));
                            break;
                        case KING:
                            images[i][j].getChildren().add(piece.getColor() == Piece.Color.WHITE ? createImageView(whiteKing) : createImageView(blackKing));
                            break;
                    }
                } else {
                    images[i][j].getChildren().add(createImageView(empty));
                }
                if (attackedSquares != null) {
                    if (attackedSquares.contains(new Position(i, j))) {
                        images[i][j].getChildren().add(createImageView(attacked));
                    }
                }
                if (illegalMoves != null) {
                    if (illegalMoves.contains(new Position(i, j))) {
                        images[i][j].getChildren().add(createImageView(illegalAttack));
                    }
                }

            }
        }
        if (game.isPieceSelected()) {
            Position selectedPosition = game.getSelectedPiece();
            images[selectedPosition.getRow()][selectedPosition.getCol()].getChildren().add(createImageView(selected));
        }

    }

    private ImageView createImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(pieceWidth);
        imageView.setFitHeight(pieceWidth);
        return imageView;
    }


}
