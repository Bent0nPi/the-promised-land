package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import ru.nsu.ccfit.chumak.model.Building;
import ru.nsu.ccfit.chumak.model.Point;
import ru.nsu.ccfit.chumak.model.Tile;
import ru.nsu.ccfit.chumak.model.Worker;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;

import java.util.ArrayList;

public class MapPane extends StackPane {
    private int height;
    private int width;
    private Pane tilesPane = new Pane();
    private Pane unitsPane = new Pane();
    static Point2D cameraPosition = new Point2D(0, 0);
    private static boolean cameraIsBlocked = true;
    GamePresenter presenter;
    private final ArrayList<TileView> tiles = new ArrayList<>();
    private final ArrayList<WorkPointView> workPoints = new ArrayList<>();
    private final ArrayList<WorkerView> workers = new ArrayList<>();
    private TileView block = null;

    public MapPane(int width, int height, GamePresenter presenter) {
        super();
        this.presenter = presenter;
        this.width = width;
        this.height = height;
        cameraPosition = cameraPosition.add(-width/2, -height / 2);
        getChildren().addAll(tilesPane, unitsPane);
    }

    static public Point2D getCameraPosition() {
        return cameraPosition;
    }

    public ArrayList<TileView> getTiles() {
        return tiles;
    }

    public ArrayList<WorkPointView> getWorkPoints() {
        return workPoints;
    }

    public ArrayList<WorkerView> getWorkers() {
        return workers;
    }

    public void addTile(Tile tile){
        TileView nextTile = new TileView(tile, cameraPosition);
        nextTile.update(tile);
        nextTile.getView().setFitHeight(240);
        nextTile.getView().setFitWidth(240);
        tiles.add(nextTile);
        tilesPane.getChildren().add(nextTile.getView());
    }

    public void addWorkPoint(Building building, int playerNum, Point basePoint){
        WorkPointView nextWorkPoint = new WorkPointView(building, playerNum, cameraPosition, basePoint );
        nextWorkPoint.getWorkPointEntity().resize(0.4);
        workPoints.add(nextWorkPoint);
        unitsPane.getChildren().add(nextWorkPoint.getView());
    }

    public void removeWorkPoints(){
        for (WorkPointView workPoint : workPoints) {
            unitsPane.getChildren().remove(workPoint.getView());
        }
        workPoints.clear();
    }

    public void addWorker(Worker worker, int playerNum, Building building){
        WorkerView nextWorker = new WorkerView(worker, playerNum, building, cameraPosition, true);
        workers.add(nextWorker);
        unitsPane.getChildren().add(nextWorker.getView());
    }

    public WorkPointView getWorkPoint(Building building){
        for (WorkPointView workPoint : workPoints) {
            if (workPoint.getBuilding().equals(building)){
                return workPoint;
            }
        }
        return null;
    }

    static public void changeCameraPosition(double diffX, double diffY){
        if(!cameraIsBlocked) {
            cameraPosition = cameraPosition.add(diffX, diffY);
        }
    }

    public void setCameraPositionToBegin(){
        if(!cameraIsBlocked) {
            cameraPosition = new Point2D(-width/2, -height / 2);
        }
    }

    static public void setCameraBlock(boolean block){
        cameraIsBlocked = block;
    }

    public void update(){
        for(TileView tileView : tiles){
            tileView.timeUpdate(cameraPosition);
        }
        for(WorkPointView workPointView : workPoints){
            workPointView.timeUpdate(cameraPosition);
        }
        for(WorkerView workerView : workers){
            workerView.timeUpdate(cameraPosition);
        }

    }

    public void addShield(boolean correct, double x, double y){
        if(correct){
            block = new TileView(new Tile("/graphics/turnInterface/available_tile.png",new Point(x, y)), cameraPosition);
        } else{
            block = new TileView(new Tile("/graphics/turnInterface/blocked_tile.png",new Point(x, y)), cameraPosition);
        }
        block.getView().setFitHeight(240);
        block.getView().setFitWidth(240);
        tiles.add(block);
        tilesPane.getChildren().add(block.getView());
    }

    public void removeShield(){
        if(block != null){
            tilesPane.getChildren().remove(block.getView());
            tiles.remove(block);
            block = null;
        }

    }

    public void clear() {
        tilesPane.getChildren().clear();
        tiles.clear();
        unitsPane.getChildren().clear();
        workPoints.clear();
        workers.clear();
    }
}
