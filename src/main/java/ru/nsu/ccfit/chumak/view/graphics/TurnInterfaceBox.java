package ru.nsu.ccfit.chumak.view.graphics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import ru.nsu.ccfit.chumak.model.Player;
import ru.nsu.ccfit.chumak.model.Tile;
import ru.nsu.ccfit.chumak.model.Worker;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;
import ru.nsu.ccfit.chumak.util.Observer;

import java.io.IOException;
import java.util.Objects;

public class TurnInterfaceBox extends VBox implements Observer<Player> {
    private int height;
    private int width;
    GamePresenter presenter;


    private TileView tileView = null;
    private WorkerView workerView = null;


    private ImageView tileImage = new ImageView();

    private ImageView workerImage = new ImageView();

    private StackPane tilePane = new StackPane();

    private StackPane workerPane = new StackPane();


    private Label playerName = new Label("Player");
    private Label score = new Label("0");
    private Label workerCount = new Label("0");

    public TurnInterfaceBox(int width, int height, GamePresenter presenter) {
        super();
        this.presenter = presenter;

        playerName.setStyle("-fx-font-weight: bold");
        playerName.setStyle("-fx-font-size: 48px;");


        Label scoreLabel = new Label("Score:");
        scoreLabel.setStyle("-fx-font-weight: bold");
        scoreLabel.setStyle("-fx-font-size: 36px;");
        scoreLabel.setPrefWidth(width*1.2);
        scoreLabel.setAlignment(Pos.TOP_RIGHT);



        score.setStyle("-fx-font-size: 36px;");
        score.setPrefWidth(width/1.25 );

        HBox  scoreBox = new HBox();
        scoreBox.setSpacing(10);
        scoreBox.setStyle("-fx-font-size: 36px;");
        scoreBox.getChildren().addAll(scoreLabel, score);

        Label workerLabel = new Label("Worker");
        workerLabel.setStyle("-fx-font-weight: bold");
        workerLabel.setStyle("-fx-font-size: 36px;");

        workerImage.setFitWidth(192);
        workerImage.setFitHeight(192);

        workerImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                workerImage.setVisible(false);
                presenter.pickWorker();
            }
        });
        workerPane.setPrefHeight(240);
        workerPane.getChildren().addAll(workerImage);
        workerPane.setBackground(new Background(new BackgroundImage(
                new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/worker_background.png")).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));


        Label workerCountLabel = new Label("Workers left:");
        workerCountLabel.setStyle("-fx-font-weight: bold");
        workerCountLabel.setStyle("-fx-font-size: 36px;");
        workerCountLabel.setPrefWidth(width * 2);
        workerCountLabel.setAlignment(Pos.TOP_RIGHT);


        workerCount.setStyle("-fx-font-size: 36px;");
        workerCount.setPrefWidth(width);

        HBox  workerCountBox = new HBox();
        workerCountBox.setSpacing(10);
        workerCountBox.setStyle("-fx-font-size: 36px;");
        workerCountBox.getChildren().addAll(workerCountLabel, workerCount);
        workerCountBox.setPrefWidth(width);


        Label currentTileLabel = new Label("Current Tile");
        currentTileLabel.setStyle("-fx-font-weight: bold");
        currentTileLabel.setStyle("-fx-font-size: 36px;");


        tileImage.setFitHeight(240);
        tileImage.setFitWidth(240);

        tileImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tileImage.setVisible(false);
                presenter.pickTile();
            }
        });


        tilePane.setPrefHeight(240);
        tilePane.getChildren().addAll(tileImage);

        tilePane.setBackground(new Background(new BackgroundImage(
                new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/wrapper.png")).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(240,240, false, false,false,false))));


        Button endTurnButton = new Button("End Turn");
        endTurnButton.setWrapText(true);
        endTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                presenter.nextTurn();
            }
        });
        endTurnButton.setStyle("-fx-font-weight: bold");
        endTurnButton.setStyle("-fx-font-size: 18px;");

        setSpacing(25);
        setAlignment(Pos.TOP_CENTER);

        getChildren().addAll(
                playerName,
                scoreBox,
                workerLabel,
                workerPane,
                workerCountBox,
                currentTileLabel,
                tilePane,
                endTurnButton
        );

        setBackground(new Background(new BackgroundImage(
                new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/part.png")).toExternalForm()),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }

    public void setTileView(Tile tile) {
        tileView = new TileView(tile, new Point2D(0,0));
        tileImage = tileView.getView();
        tileImage.setFitHeight(240);
        tileImage.setFitWidth(240);
        tileImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                presenter.pickTile();
            }
        });
        tilePane.getChildren().remove(0);
        tilePane.getChildren().add(tileImage);
    }

    public void setWorkerView(Worker worker, int playerNumber) {
        workerView = new WorkerView(worker, playerNumber, null, new Point2D(0,0), false);
        workerImage = workerView.getView();
        workerImage.setFitHeight(192);
        workerImage.setFitWidth(192);
        workerImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                presenter.pickWorker();
            }
        });
        workerPane.getChildren().remove(0);
        workerPane.getChildren().add(workerImage);
    }

    public void hideWorkerView() {
        workerImage.setVisible(false);
    }

    public void hideTileImage(){
        tileImage.setVisible(false);
    }

    public void returnTile(){
        tileImage.setVisible(true);
    }

    public void returnWorker(){
        workerImage.setVisible(true);
    }

    public void update(){
        if(workerView != null) {
            workerView.timeUpdate(new Point2D(0,0));
        }
    }

    @Override
    public void update(Player player) {
        playerName.setText(player.getName());
        score.setText(String.valueOf(player.getScore()));
        workerCount.setText(String.valueOf(player.getWorkersCount()));
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
