import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

/**
 * author: Caojiajun.
 * Used to display the graphical interface of AnimalChess.
 */
public class GUI extends Application {
    private static History historyRecord;
    private static Button[][] cells = new Button[7][9];
    private static Text turn;
    private static Text left;
    private static Text right;
    MediaPlayer defaultMusic = new MediaPlayer(new Media("file:/Users/DELL/IdeaProjects/Jungle/audio/MainTheme.mp3"));

    @Override
    /**
     * Used to display the main stage of the game, set the buttons and texts.
     */
    public void start(Stage primaryStage) throws FileNotFoundException, CloneNotSupportedException {
        HBox labels = new HBox();
        Button bgm = new Button("Background Music");
        Button help = new Button("help");
        Button clear = new Button("clear");
        Button undo = new Button("undo");
        Button restart = new Button("restart");
        Button exit = new Button("exit");
        left = new Text("8");
        left.setFill(Color.RED);
        Text vs = new Text("VS");
        right = new Text("8");
        right.setFill(Color.BLUE);
        turn = new Text(" turn:  left  player");
        turn.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, 17));
        turn.setFill(Color.color(0.8, 0, 0));
        labels.getChildren().addAll(bgm, help, clear, undo, restart, exit, left, vs, right, turn);
        labels.setMargin(bgm, new Insets(0, 0, 0, 6));
        labels.setMargin(exit, new Insets(0, 120, 0, 0));
        labels.setSpacing(35);

        GridPane animals = new GridPane();
        init(animals);
        ImageView mapImage = new ImageView(new Image("file:pic/Map.png"));
        mapImage.setFitWidth(1060);
        mapImage.setFitHeight(750);
        StackPane map = new StackPane();
        map.getChildren().addAll(mapImage, animals);

        VBox pane = new VBox();
        pane.getChildren().addAll(map, labels);

        bgm.setOnAction(b -> {
            if (bgm.getText().equals("Background Music")) {
                defaultMusic.play();
                defaultMusic.setCycleCount(Timeline.INDEFINITE);
                bgm.setText("          pause          ");
            } else {
                defaultMusic.pause();
                bgm.setText("Background Music");
            }
        });

        help.setOnAction(h -> {
            ImageView helpPic = new ImageView(new Image("file:pic/help.png"));
            map.getChildren().add(helpPic);
            helpPic.setOnMouseClicked(e -> map.getChildren().remove(helpPic));
        });

        clear.setOnAction(m -> {
            if (clear.getText().equals("clear")) {
                try {
                    clear.setText("stop");
                    refresh();
                    Board board = Board.getBoard();
                    for (int r = 0; r < 7; r++) {
                        int x = r;
                        for (int c = 0; c < 9; c++) {
                            int y = c;
                            if (board.getAnimal(r, c).getPower() != -1) {
                                cells[r][c].setOnAction(modest -> {
                                    try {
                                        board.getAnimal(x, y).setPower(-1);
                                        update();
                                    } catch (FileNotFoundException e) {
                                        System.out.println("ERROR: Cannot Find Files!");
                                    }
                                });
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("ERROR: Cannot Find Files!");
                }
            } else {
                try {
                    clear.setText("clear");
                    right.setText("" + Board.getBoard().countRight());
                    left.setText("" + Board.getBoard().countLeft());
                    setEvent();
                } catch (FileNotFoundException e) {
                    System.out.println("ERROR: Cannot Find Files!");
                }
            }
        });

        undo.setOnAction(u -> {
            try {
                historyRecord = History.getHistory();
                historyRecord.undo();
                refresh();
                update();
                changePlayer();
                right.setText("" + Board.getBoard().countRight());
                left.setText("" + Board.getBoard().countLeft());
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: Cannot Find Files!");
            }
        });

        restart.setOnAction(r -> {
            try {
                Board.setBoard(null);
                update();
                turn.setText(" turn:  left  player");
                turn.setFill(Color.color(0.8, 0, 0));
                left.setText("8");
                right.setText("8");
                historyRecord = History.getHistory();
                historyRecord.restart();
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: Cannot Find Files!");
            }
        });

        exit.setOnAction(e -> {
            Text text = new Text("Are you sure to exit?");
            text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, 20));
            text.setFill(Color.BLUE);
            Button yes = new Button("Yes");
            yes.setTextFill(Color.RED);
            Button cancel = new Button("Cancel");
            cancel.setTextFill(Color.GREEN);

            HBox hBox = new HBox();
            hBox.getChildren().addAll(yes, cancel);
            hBox.setSpacing(50);
            hBox.setAlignment(Pos.CENTER);
            VBox vBox = new VBox();
            vBox.getChildren().addAll(text, hBox);

            Stage confirm = new Stage();
            confirm.setScene(new Scene(vBox));
            confirm.setResizable(false);
            confirm.show();

            DropShadow shadow = new DropShadow();
            yes.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent s) -> {
                yes.setEffect(shadow);
                defaultMusic.setCycleCount(Timeline.INDEFINITE);
                confirm.close();
                primaryStage.close();

                ImageView goodBye = new ImageView((new Image("file:pic/exit.jpg")));
                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(goodBye);
                Stage bye = new Stage();
                bye.setTitle("Goodbye~~");
                bye.setScene(new Scene(stackPane));
                bye.setResizable(false);
                MediaPlayer byeMusic = new MediaPlayer(new Media("file:/Users/DELL/IdeaProjects/Jungle/audio/上海滩.mp3"));
                byeMusic.play();
                bye.show();
            });

            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent s) -> {
                cancel.setEffect(shadow);
                confirm.close();
            });
        });

        setEvent();

        primaryStage.setTitle("AnimalChess");
        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.hide();

        ImageView beginPic = new ImageView(new Image("file:pic/begin.jpg"));
        MediaPlayer beginMusic = new MediaPlayer(new Media("file:/Users/DELL/IdeaProjects/Jungle/audio/TryEverything.mp3"));
        beginMusic.play();
        Button go = new Button("GO");
        go.setTextFill(Color.GREEN);
        Button withdraw = new Button("EXIT");
        withdraw.setTextFill(Color.RED);

        HBox choose = new HBox();
        choose.getChildren().addAll(go, withdraw);
        choose.setAlignment(Pos.CENTER);
        choose.setSpacing(100);
        VBox beginner = new VBox();
        beginner.getChildren().addAll(beginPic, choose);
        beginner.setAlignment(Pos.CENTER);

        Stage begin = new Stage();
        begin.setTitle("Welcome To AnimalChess!");
        begin.setScene(new Scene(beginner));
        begin.setResizable(false);
        begin.show();

        go.setOnAction(g -> {
            beginMusic.pause();
            primaryStage.show();
            begin.close();
        });
        withdraw.setOnAction(w -> System.exit(0));
    }

    private void init(GridPane animals) throws FileNotFoundException {
        setStyle(animals);

        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c] = new Button();
            }
        }

        update();

        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 9; c++) {
                animals.add(cells[r][c], c, r);
            }
        }
    }

    private void setEvent() throws FileNotFoundException {
        Board board = Board.getBoard();

        for (int r = 0; r < 7; r++) {
            int x = r;
            for (int c = 0; c < 9; c++) {
                int y = c;
                cells[r][c].setOnAction(e0 -> {
                    try {
                        refresh();
                        if (board.getAnimal(x, y).getPower() != -1 && ((turn.getText().equals(" turn:  left  player") && board.getAnimal(x, y).getCamp())
                                || (turn.getText().equals(" turn:  right player") && !board.getAnimal(x, y).getCamp()))) {
                            cells[x][y].setBackground(new Background(new BackgroundFill(new Color(0, 0.5, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                            MediaPlayer music = new MediaPlayer(new javafx.scene.media.Media("file:/Users/DELL/IdeaProjects/Jungle/audio/" + board.getAnimal(x, y).getPower() + ".mp3"));
                            music.play();

                            if (board.getAnimal(x, y).getAnimalType() == Animal.AnimalType.MOUSE) {
                                AnimalCanSwim mouse = new AnimalCanSwim(board.getAnimal(x, y).getCamp(), 1, x, y, Animal.AnimalType.MOUSE);
                                boolean[] canMove1 = mouse.judge();

                                if (canMove1[0])
                                    move(cells[x][y], cells[x - 1][y], x, y, x - 1, y);
                                if (canMove1[1])
                                    move(cells[x][y], cells[x][y + 1], x, y, x, y + 1);
                                if (canMove1[2])
                                    move(cells[x][y], cells[x + 1][y], x, y, x + 1, y);
                                if (canMove1[3])
                                    move(cells[x][y], cells[x][y - 1], x, y, x, y - 1);

                            } else if (board.getAnimal(x, y).getAnimalType() == Animal.AnimalType.TIGER ||
                                    board.getAnimal(x, y).getAnimalType() == Animal.AnimalType.LION) {
                                AnimalCanJump animalCanJump = new AnimalCanJump(board.getAnimal(x, y).getCamp(),
                                        board.getAnimal(x, y).getPower(), x, y, board.getAnimal(x, y).getAnimalType());
                                boolean[] canMove2 = animalCanJump.judge();

                                if (canMove2[0]) {
                                    if (board.getTileArray()[x - 1][y].getTileType() == Tile.TileType.RIVER)
                                        move(cells[x][y], cells[x - 3][y], x, y, x - 3, y);
                                    else
                                        move(cells[x][y], cells[x - 1][y], x, y, x - 1, y);
                                }
                                if (canMove2[1]) {
                                    if (board.getTileArray()[x][y + 1].getTileType() == Tile.TileType.RIVER)
                                        move(cells[x][y], cells[x][y + 4], x, y, x, y + 4);
                                    else
                                        move(cells[x][y], cells[x][y + 1], x, y, x, y + 1);
                                }
                                if (canMove2[2]) {
                                    if (board.getTileArray()[x + 1][y].getTileType() == Tile.TileType.RIVER)
                                        move(cells[x][y], cells[x + 3][y], x, y, x + 3, y);
                                    else
                                        move(cells[x][y], cells[x + 1][y], x, y, x + 1, y);
                                }
                                if (canMove2[3]) {
                                    if (board.getTileArray()[x][y - 1].getTileType() == Tile.TileType.RIVER)
                                        move(cells[x][y], cells[x][y - 4], x, y, x, y - 4);
                                    else
                                        move(cells[x][y], cells[x][y - 1], x, y, x, y - 1);
                                }

                            } else {
                                AnimalOrdinary animalOrdinary = new AnimalOrdinary(board.getAnimal(x, y).getCamp(),
                                        board.getAnimal(x, y).getPower(), x, y, board.getAnimal(x, y).getAnimalType());
                                boolean[] canMove3 = animalOrdinary.judge();

                                if (canMove3[0])
                                    move(cells[x][y], cells[x - 1][y], x, y, x - 1, y);
                                if (canMove3[1])
                                    move(cells[x][y], cells[x][y + 1], x, y, x, y + 1);
                                if (canMove3[2])
                                    move(cells[x][y], cells[x + 1][y], x, y, x + 1, y);
                                if (canMove3[3])
                                    move(cells[x][y], cells[x][y - 1], x, y, x, y - 1);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("ERROR: Cannot Find Files!");
                    }
                });
            }
        }
    }

    /**
     * Used to connect the move of GUI with the logical board.
     * When a valid event is triggered, the logical board will update accordingly.
     */
    private void move(Button from, Button to, int fromX, int fromY, int toX, int toY) {
        to.setBackground(new Background(new BackgroundFill(new Color(0.5, 0, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
        to.setOnAction(e2 -> {
            to.setGraphic(from.getGraphic());
            from.setGraphic(null);
            changePlayer();

            try {
                Board board = Board.getBoard();
                historyRecord = History.getHistory();
                historyRecord.addRecord(board.getAnimal(fromX, fromY), board.getAnimal(toX, toY));
                board.getAnimal(fromX, fromY).eat(board.getAnimal(toX, toY));
                right.setText("" + board.countRight());
                left.setText("" + board.countLeft());

                if (board.isWin(board.getAnimal(toX, toY))) {
                    if (board.getAnimal(toX, toY).getCamp()) {
                        turn.setText(" winner: left player");
                        turn.setFill(Color.color(0.8, 0, 0));
                        defaultMusic.pause();
                        MediaPlayer winner = new MediaPlayer(new Media("file:/Users/DELL/IdeaProjects/Jungle/audio/victory.mp3"));
                        winner.play();

                        ImageView victoryPic = new ImageView(new Image("file:pic/victory.jpg"));
                        victoryPic.setFitWidth(100);
                        victoryPic.setFitHeight(100);
                        Text victoryText = new Text("Victory: Left");
                        victoryText.setFill(Color.RED);
                        victoryText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, 30));
                        HBox victory = new HBox();
                        victory.getChildren().addAll(victoryPic, victoryText);
                        victory.setAlignment(Pos.CENTER);
                        Text reason = new Text();
                        if (board.isAceTheEnemy(board.getAnimal(toX, toY)))
                            reason.setText("Reason: the enemy are all slain, ACE!");
                        else if (board.isAliveEnemyCannotMove(board.getAnimal(toX, toY)))
                            reason.setText("Reason: the enemy are all trapped!");
                        else reason.setText("Reason: the enemy's home has be occupied!");
                        reason.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, 20));
                        Text introduction = new Text("click \"restart\" to start a new game OR click \"exit\" to stop the game.");
                        VBox win = new VBox();
                        win.getChildren().addAll(victory, reason, introduction);
                        win.setAlignment(Pos.CENTER);
                        Stage winStage = new Stage();
                        winStage.setScene(new Scene(win));
                        winStage.setResizable(false);
                        winStage.show();
                    } else {
                        turn.setText(" winner: right player");
                        turn.setFill(Color.color(0, 0, 0.75));
                        defaultMusic.pause();
                        MediaPlayer winner = new MediaPlayer(new Media("file:/Users/DELL/IdeaProjects/Jungle/audio/victory.mp3"));
                        winner.play();

                        ImageView victoryPic = new ImageView(new Image("file:pic/victory.jpg"));
                        victoryPic.setFitWidth(100);
                        victoryPic.setFitHeight(100);
                        Text victoryText = new Text("Victory: Right");
                        victoryText.setFill(Color.RED);
                        victoryText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, 30));
                        HBox victory = new HBox();
                        victory.getChildren().addAll(victoryPic, victoryText);
                        victory.setAlignment(Pos.CENTER);
                        Text reason = new Text();
                        if (board.isAceTheEnemy(board.getAnimal(toX, toY)))
                            reason.setText("Reason: the enemy are all slain, ACE!");
                        else if (board.isAliveEnemyCannotMove(board.getAnimal(toX, toY)))
                            reason.setText("Reason: the enemy are all trapped!");
                        else reason.setText("Reason: the enemy's home has be occupied!");
                        reason.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, 20));
                        Text introduction = new Text("click \"restart\" to start a new game OR click \"exit\" to stop the game.");
                        VBox win = new VBox();
                        win.getChildren().addAll(victory, reason, introduction);
                        win.setAlignment(Pos.CENTER);
                        Stage winStage = new Stage();
                        winStage.setScene(new Scene(win));
                        winStage.setResizable(false);
                        winStage.show();
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: Cannot Find Files!");
            }

            try {
                update();
                setEvent();
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: Cannot Find Files!");
            }
        });
    }

    private void update() throws FileNotFoundException {
        ImageView[] animalLeft = new ImageView[8];
        ImageView[] animalRight = new ImageView[8];
        for (int i = 1; i <= 8; i++) {
            animalLeft[i - 1] = new ImageView(new Image("file:pic/animals/left/" + i + "Left.png"));
            animalLeft[i - 1].setFitWidth(90);
            animalLeft[i - 1].setFitHeight(80);

            animalRight[i - 1] = new ImageView(new Image("file:pic/animals/right/" + i + "Right.png"));
            animalRight[i - 1].setFitWidth(90);
            animalRight[i - 1].setFitHeight(80);
        }

        Board board = Board.getBoard();
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].setPrefSize(90, 90);
                cells[r][c].setBackground(new Background(new BackgroundFill(new Color(0, 0.2, 0.1, 0), CornerRadii.EMPTY, Insets.EMPTY)));
                if (board.getAnimal(r, c).getCamp() && board.getAnimal(r, c).getPower() != -1)
                    cells[r][c].setGraphic(animalLeft[board.getAnimal(r, c).getPower() - 1]);
                else if (!board.getAnimal(r, c).getCamp() && board.getAnimal(r, c).getPower() != -1)
                    cells[r][c].setGraphic(animalRight[board.getAnimal(r, c).getPower() - 1]);
                else cells[r][c].setGraphic(null);
            }
        }
    }

    private void refresh() {
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].setBackground(new Background(new BackgroundFill(new Color(0, 0.2, 0.1, 0), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    private void changePlayer() {
        if (turn.getText().equals(" turn:  left  player")) {
            turn.setText(" turn:  right player");
            turn.setFill(Color.color(0, 0, 0.75));
        } else {
            turn.setText(" turn:  left  player");
            turn.setFill(Color.color(0.8, 0, 0));
        }
    }

    private void setStyle(GridPane gridPane) {
        gridPane.setPadding(new Insets(120, 123, 20, 142));

        for (int r = 0; r < 7; r++) {
            RowConstraints row = new RowConstraints(87);
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
        }

        for (int c = 0; c < 9; c++) {
            ColumnConstraints column = new ColumnConstraints(87);
            column.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(column);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
