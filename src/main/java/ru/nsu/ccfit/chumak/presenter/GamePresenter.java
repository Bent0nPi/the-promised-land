package ru.nsu.ccfit.chumak.presenter;

import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.model.*;
import ru.nsu.ccfit.chumak.util.GameUtil;
import ru.nsu.ccfit.chumak.util.PlayerTypes;
import ru.nsu.ccfit.chumak.view.controllers.Keyboard;
import ru.nsu.ccfit.chumak.view.controllers.Mouse;
import ru.nsu.ccfit.chumak.view.graphics.GamePane;
import ru.nsu.ccfit.chumak.view.graphics.MapPane;
import ru.nsu.ccfit.chumak.view.graphics.WorkPointView;

import java.util.ArrayList;

public class GamePresenter {

    private final static Logger logger = LogManager.getLogger(GamePresenter.class);

    private GameModel model;
    private GamePane gamePane;
    private boolean currentTileIsPicked = false;
    private boolean currentWorkerIsPicked = false;

    private Tile currentTile;
    private Worker currentWorker;
    private Map.Coords currentMapCoords = null;
    private boolean canPushTile = false;
    private Tile testTile = null;
    boolean currentTileRotated = false;
    boolean currentTileIsPlaced = false;

    private double sensorBarrier = 10000;
    private Building currentBuilding = null;
    private double minimalDistance;


    private double totalScroll = 0;


    public GamePresenter(GameModel model, GamePane gamePane) {
        this.model = model;
        this.gamePane = gamePane;
    }

    public GamePresenter() {
        this.gamePane = null;
        this.model = null;
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    public void setGamePane(GamePane gamePane) {
        this.gamePane = gamePane;
    }


    public void setNewTile(Tile tile){
        gamePane.getTurnInterfaceBox().setTileView(tile);
        currentTile = tile;
        currentTileIsPicked = false;
        currentTileIsPlaced = false;
    }

    public void pickTile(){
        gamePane.getTurnInterfaceBox().hideTileImage();
        currentTileIsPicked = true;
        gamePane.addTile(currentTile,Mouse.last);
    }

    public void returnTile(){
        currentTileIsPicked = false;
        canPushTile = false;
        gamePane.getMapPane().removeShield();
        gamePane.returnTile();
        gamePane.getTurnInterfaceBox().returnTile();
    }

    public void setNewWorker(Worker worker){
        currentWorker = worker;
        gamePane.getTurnInterfaceBox().setWorkerView(worker, model.getSession().getCurrentPlayerIdx());
        currentWorkerIsPicked = false;
    }

    public void pickWorker(){
        if (currentTileIsPlaced){
            gamePane.getTurnInterfaceBox().hideWorkerView();
            currentWorkerIsPicked = true;
            gamePane.addWorker(currentWorker,model.getSession().getCurrentPlayerIdx(),Mouse.last);
        }
    }

    public void returnWorker(){
        if(currentWorkerIsPicked){
            currentWorkerIsPicked = false;
            if(currentBuilding != null){
                WorkPointView lastWorkPoint = gamePane.getMapPane().getWorkPoint(currentBuilding);
                lastWorkPoint.getWorkPointEntity().resize(0.4);
                currentBuilding = null;
            }
            gamePane.returnWorker();
            gamePane.getTurnInterfaceBox().returnWorker();
        }
    }


    public void update() {

        if (currentTileIsPicked) {
            currentTileRotated = false;

            if(Mouse.scrollDirection != 0){
                if(Mouse.scrollDirection > 0 && totalScroll < 0 || Mouse.scrollDirection < 0 && totalScroll > 0){
                    totalScroll = 0;
                }
                totalScroll += Mouse.scrollDirection;
                Mouse.scrollDirection = 0;
                if(totalScroll > 120){
                    totalScroll = 0;
                    currentTile.rotate(90);
                    currentTileRotated = true;
                } else if(totalScroll < -120){
                    totalScroll = 0;
                    currentTile.rotate(-90);
                    currentTileRotated = true;
                }
            }

            currentTile.setBasePoint(new Point(Mouse.last.getX(), Mouse.last.getY()));

            Map.Coords coords = model.getSession().getGameMap().transformToCoords(Mouse.last.getX() + MapPane.getCameraPosition().getX(), Mouse.last.getY()+ MapPane.getCameraPosition().getY());
            if(currentMapCoords == null || currentMapCoords.getX() != coords.getX() || currentMapCoords.getY() != coords.getY() || currentTileRotated){
                if(currentMapCoords != null){
                    gamePane.getMapPane().removeShield();
                }
                currentMapCoords = coords;
                boolean isAvailableAndTouched = model.getSession().getGameMap().isAvailable(coords.getX(), coords.getY())
                        && model.getSession().getGameMap().hasNeighbours(coords.getX(), coords.getY());

                if(isAvailableAndTouched){
                    if(testTile != null){
                        testTile.setBasePoint(new Point(coords.getX() * GameUtil.getTileSizeX(),coords.getY() * GameUtil.getTileSizeY()));
                    } else {
                        Shape[] tmpShapes = new Shape[currentTile.getShapes().length];
                        for(int i = 0; i < currentTile.getShapes().length; i++){
                            tmpShapes[i] = new Shape(currentTile.getShapes()[i].getVertices(), new Point(coords.getX() * GameUtil.getTileSizeX(),coords.getY() * GameUtil.getTileSizeY()),currentTile.getShapes()[i].getTag());
                        }

                        testTile = new Tile(tmpShapes, currentTile.getSpecialPoints(), currentTile.getBuildings(), new Point(coords.getX() * GameUtil.getTileSizeX(),coords.getY() * GameUtil.getTileSizeY()), currentTile.getResource());

                        testTile.setBasePoint(new Point(coords.getX() * GameUtil.getTileSizeX(),coords.getY() * GameUtil.getTileSizeY()));
                    }


                    if(model.getSession().getGameMap().isCompatible(coords.getX(),coords.getY(),testTile)){
                        canPushTile = true;

                    } else {
                        canPushTile = false;
                    }

                    gamePane.getMapPane().addShield(canPushTile,coords.getX()* GameUtil.getTileSizeX(), coords.getY()*GameUtil.getTileSizeY() );
                } else{
                    testTile = null;
                    canPushTile = false;
                }
            }
        }
        if (currentWorkerIsPicked) {
            currentWorker.setPosition(new Point(Mouse.last.getX(), Mouse.last.getY()));
            Building nextBuilding = null;
            minimalDistance = 1000000000;
            for(Building building: currentTile.getBuildings()){
                if(building.getAvailableWorkPoint() == null){
                    continue;
                }
                double currentDistance = Math.pow(building.getAvailableWorkPoint().getX()+ currentTile.getBasePoint().getX() - currentWorker.getPosition().getX() - MapPane.getCameraPosition().getX(),2) + Math.pow(building.getAvailableWorkPoint().getY()+ currentTile.getBasePoint().getY() - currentWorker.getPosition().getY() - MapPane.getCameraPosition().getY(),2);
                if(currentDistance < minimalDistance){
                    minimalDistance = currentDistance;
                    nextBuilding = building;
                }
            }
            if (minimalDistance > sensorBarrier){
                nextBuilding = null;
            }
            if(currentBuilding != nextBuilding){
                if(currentBuilding != null){
                    WorkPointView lastWorkPoint = gamePane.getMapPane().getWorkPoint(currentBuilding);
                    lastWorkPoint.getWorkPointEntity().resize(0.4);
                }
                currentBuilding = nextBuilding;
                if(currentBuilding != null){
                    WorkPointView newWorkPoint = gamePane.getMapPane().getWorkPoint(currentBuilding);
                    newWorkPoint.getWorkPointEntity().resize(0.6);
                }
            }
        }



        // camera moving
        if(Keyboard.down(KeyCode.W)){
            gamePane.getMapPane().changeCameraPosition(0,-10);
        }
        if(Keyboard.down(KeyCode.A)){
            gamePane.getMapPane().changeCameraPosition(-10,0);
        }
        if(Keyboard.down(KeyCode.S)){
            gamePane.getMapPane().changeCameraPosition(0,10);
        }
        if(Keyboard.down(KeyCode.D)){
            gamePane.getMapPane().changeCameraPosition(10,0);
        }
        if(Keyboard.down(KeyCode.UP)){
            gamePane.getMapPane().changeCameraPosition(0,-10);
        }
        if(Keyboard.down(KeyCode.DOWN)){
            gamePane.getMapPane().changeCameraPosition(0,10);
        }
        if(Keyboard.down(KeyCode.LEFT)){
            gamePane.getMapPane().changeCameraPosition(-10,0);
        }
        if(Keyboard.down(KeyCode.RIGHT)){
            gamePane.getMapPane().changeCameraPosition(10,0);
        }

        //update coordinates of map's objects
        gamePane.getMapPane().update();
        //update data in user interface
        gamePane.getTurnInterfaceBox().update();
        //update data on frontground
        gamePane.updateObjects();
        if(gamePane.getPickedWorker() != null && gamePane.getPickedWorker().getTimeToLive()  == 0){
            gamePane.returnWorker();
        }

    }


    public void addTileOnMap(Tile tile){
        gamePane.getMapPane().addTile(tile);
    }

    public void placeTile() {
        if(canPushTile){
            gamePane.returnTile();
            currentTileIsPicked = false;
            canPushTile = false;
            Map.Coords coords = model.getSession().getGameMap().transformToCoords(Mouse.last.getX() + MapPane.getCameraPosition().getX(), Mouse.last.getY() + MapPane.getCameraPosition().getY());
            currentTile.setBasePoint(new Point(coords.getX() * GameUtil.getTileSizeX(),coords.getY() * GameUtil.getTileSizeY()));
            try {
                model.getSession().addTile(coords.getX(),coords.getY(),currentTile);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            gamePane.getMapPane().addTile(currentTile);
            currentTileIsPlaced = true;
            showWorkPoints();
        }
    }

    public void showWorkPoints(){
        for(Building building: currentTile.getBuildings()){
            if(building.getAvailableWorkPoint() != null){
                gamePane.getMapPane().addWorkPoint(building, model.getSession().getCurrentPlayerIdx(), currentTile.getBasePoint());
            }
        }
    }

    public void hideWorkPoints(){
        gamePane.getMapPane().removeWorkPoints();
    }

    public void placeWorker() {
        if(currentBuilding != null && currentWorkerIsPicked){
            currentWorker.setPosition(new Point(currentBuilding.getWorkPoint().getX() + currentTile.getBasePoint().getX(), currentBuilding.getWorkPoint().getY() + currentTile.getBasePoint().getY()));
            hideWorkPoints();
            currentWorkerIsPicked = false;
            model.getSession().addWorkerToBuilding(currentWorker,currentBuilding);
            gamePane.transformWorker();
            gamePane.getPickedWorker().getView().setLayoutX(Mouse.last.getX() - 81);
            gamePane.getPickedWorker().getView().setLayoutY(Mouse.last.getY() - 81);
            gamePane.getPickedWorker().getView().setFitHeight(192);
            gamePane.getPickedWorker().getView().setFitWidth(192);
            gamePane.getMapPane().addWorker(currentWorker,model.getSession().getCurrentPlayerIdx(),currentBuilding);
        }
    }

    public void nextTurn(){
        currentTileIsPlaced = false;
        currentWorkerIsPicked = false;
        currentTileRotated = false;
        currentBuilding = null;
        hideWorkPoints();
        model.doTurn();
    }

    public void connectSessionInterface(){
        if(model != null && gamePane != null){
            model.getSession().addObserver(gamePane.getTurnInterfaceBox());
        }
    }

    public void finishSession() {
        model.finish();
    }

    public void showResults(Player[] results) {
        gamePane.showResults(results);
        MapPane.setCameraBlock(true);
    }

    public void restart() {
        gamePane.hideAllAlerts();
        gamePane.getMapPane().clear();
        MapPane.setCameraBlock(false);
        gamePane.getMapPane().setCameraPositionToBegin();
        MapPane.setCameraBlock(true);
        gamePane.showSettings();
    }

    public void startGame(ArrayList players) {
        gamePane.hideAllAlerts();
        MapPane.setCameraBlock(false);
        PlayerTypes[] types = new PlayerTypes[players.size()];
        String[] names = new String[players.size()];
        for(int i = 0; i < players.size(); i++){
            types[i] = PlayerTypes.OFFLINE;
            names[i] = players.get(i).toString();
        }
        model.choosePlayers(names,types);
        model.startSession();
    }
}
