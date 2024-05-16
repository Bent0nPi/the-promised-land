package ru.nsu.ccfit.chumak.view.graphics;


import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.model.Player;
import ru.nsu.ccfit.chumak.model.Tile;
import ru.nsu.ccfit.chumak.model.Worker;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;

public class GamePane extends StackPane {

    private final static Logger logger = LogManager.getLogger(GamePane.class);

    private final AnimationTimer timer;
    private MapPane mapPane;
    private TurnInterfaceBox turnInterfaceBox;
    GamePresenter presenter;
    TileView pickedTile = null;
    WorkerView pickedWorker = null;
    private double width;
    private double height;


    public GamePane(int width, int height, GamePresenter presenter) {
        this.width = width;
        this.height = height;
        this.presenter = presenter;
        presenter.setGamePane(this);
        mapPane = new MapPane(width, height, presenter);
        turnInterfaceBox = new TurnInterfaceBox(width / 10, height, presenter);
        HBox hBox = new HBox();
        SettingsPane playersSettings = new SettingsPane(presenter);
        hBox.getChildren().addAll(turnInterfaceBox);
        this.getChildren().addAll(mapPane, hBox, playersSettings);
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        start();
    }

    public void update() {
        presenter.update();

    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    public MapPane getMapPane() {
        return mapPane;
    }
    public TurnInterfaceBox getTurnInterfaceBox() {
        return turnInterfaceBox;
    }


    public void addTile(Tile tile, Point2D position) {
        pickedTile = new TileView(tile,new Point2D(0,0));
        pickedTile.getView().setFitHeight(240);
        pickedTile.getView().setFitWidth(240);
        pickedTile.getTileEntity().setPosition(position);
        Pane space = new Pane();
        getChildren().add(space);
        space.getChildren().add(pickedTile.getView());
    }

    public void addWorker(Worker worker, int playerNumber, Point2D position) {
        pickedWorker = new WorkerView(worker, playerNumber, null, new Point2D(0,0), false);
        pickedWorker.getView().setFitHeight(192);
        pickedWorker.getView().setFitWidth(192);
        pickedWorker.getWorkerEntity().setPosition(position);
        pickedWorker.getWorkerEntity().setAnimation(new Animation(pickedWorker.getWorkerEntity().getSprites().getRow(5), false, true));
        Pane space = new Pane();
        getChildren().add(space);
        space.getChildren().add(pickedWorker.getView());
    }

    public void transformWorker(){
        pickedWorker.playDead();
    }

    public void returnTile(){
        getChildren().remove(2);
        pickedTile = null;
    }

    public void returnWorker(){
        getChildren().remove(2);
        pickedWorker = null;
    }

    public TileView getPickedTile() {
        return pickedTile;
    }

    public WorkerView getPickedWorker() {
        return pickedWorker;
    }

    public void updateObjects() {
        if(pickedWorker != null) {
            pickedWorker.timeUpdate(new Point2D(0,0));
        }
        if(pickedTile != null) {
            pickedTile.timeUpdate(new Point2D(0,0));
        }
    }

    public void showResults(Player[] results) {
        BorderPane layer = new BorderPane();
        ResultsPane resultsPane = new ResultsPane(results, width, height, presenter);
        layer.setCenter(resultsPane);
        getChildren().add(layer);
    }

    public void hideAllAlerts() {
        getChildren().remove(2);
    }

    public void showSettings(){
        SettingsPane playersSettings = new SettingsPane(presenter);
        getChildren().add(playersSettings);
    }

}
