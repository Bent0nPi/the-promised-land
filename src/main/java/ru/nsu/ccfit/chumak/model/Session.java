package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;
import ru.nsu.ccfit.chumak.util.GameUtil;
import ru.nsu.ccfit.chumak.util.Observable;
import ru.nsu.ccfit.chumak.util.Observer;
import ru.nsu.ccfit.chumak.util.exceptions.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Session implements Observable<Player> {

    private final static Logger logger = LogManager.getLogger(Session.class);

    private final Player[] players;
    private List<Observer<Player>> observers = new ArrayList<>();
    private int currentPlayerIdx = -1;
    private final Map gameMap;
    private final TilesDeck deck;
    private final Judge judge = new Judge();
    private final GamePresenter gamePresenter;

    public Session(Player[] players, GamePresenter presenter) {
        this.players = players;
        gamePresenter = presenter;
        logger.info("Start loading tiles");
        ArrayList<Tile> tilesPool = prepareTiles();
        Tile firstTile = tilesPool.getFirst();
        tilesPool.removeFirst();
        logger.info("Creating map");
        gameMap = new Map(GameUtil.getTileSizeX(), GameUtil.getTileSizeY(), firstTile);
        logger.info("Creating deck");
        this.deck = new OfflineTilesDeck(tilesPool);
        logger.info("Start adding first tile");
        gamePresenter.addTileOnMap(firstTile);
    }

    public void addWorkerToBuilding(Worker currentWorker, Building currentBuilding) {
        currentBuilding.addWorker(currentWorker);
        notifyObservers();
    }

    public class Judge {
        public void countTileScores(Tile tile, boolean isFinish) {
            logger.info("Start counting tile scores");
            checkBuildings(isFinish, tile);
            Map.Coords coords = gameMap.transformToCoords(tile.getBasePoint().getX(), tile.getBasePoint().getY());
            logger.info("Check neighbour tiles, tile's coords are " + coords.getX() + " " + coords.getY());
            Tile currentTile = null;
            if((currentTile = gameMap.getTile(coords.getX() -1, coords.getY()))!= null){
                checkBuildings(isFinish, currentTile);
            }
            if((currentTile = gameMap.getTile(coords.getX() + 1, coords.getY()))!= null){
                checkBuildings(isFinish, currentTile);
            }
            if((currentTile = gameMap.getTile(coords.getX(), coords.getY()-1))!= null){
                checkBuildings(isFinish, currentTile);
            }
            if((currentTile = gameMap.getTile(coords.getX(), coords.getY()+1))!= null){
                checkBuildings(isFinish, currentTile);
            }

        }

        private void checkBuildings(boolean isFinish, Tile currentTile) {
            logger.info("Count building scores");
            for (Building building : currentTile.getBuildings()) {
                if(building.isFinished() || isFinish){
                    logger.info(building + " finished: " + building.isFinished());
                    String[] owners = building.getOwners();
                    int income = building.getScore(isFinish);
                    logger.info("remove workers from building " + building);
                    building.removeWorkers();
                    building.removeWorkPoints();
                    for (Player player : players) {
                        for(String owner : owners) {
                            if(owner.equals(player.getName())){
                                logger.info("added" + income + "to " + player.getName());
                                player.incrementScore(income);
                            }
                        }
                    }
                }
            }
        }

        public void countMapScores() {
            Set<Tile> usedTiles = new HashSet<>();
            Queue<Tile> TilesQueue = new LinkedList<>();
            TilesQueue.add(gameMap.getTile(0,0));
            while (!TilesQueue.isEmpty()) {
                Tile tile = TilesQueue.remove();
                countTileScores(tile, true);
                usedTiles.add(tile);
                Tile[] neighbours = gameMap.getNeighbours(tile);
                for (Tile neighbour : neighbours) {
                    if(neighbour != null && !usedTiles.contains(neighbour)){
                        TilesQueue.add(neighbour);
                    }
                }
            }
        }
    }

    private ArrayList<Tile> prepareTiles(){
        int cntTiles = GameUtil.getCountTiles();
        String tilesDir = GameUtil.getTileModelResourceDir();
        ArrayList<Tile> tilesPool = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        String currentTileName = tilesDir + "1.json";
        try {
            for (int i = 1; i <= cntTiles; i++) {
                currentTileName = tilesDir + i + ".json";
                Tile nextTile = objectMapper.readValue(new File(currentTileName), Tile.class);
                tilesPool.add(nextTile);
            }
        } catch (JsonProcessingException e) {
            throw new DataAccessException(currentTileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tilesPool;
    }

    public void doNextTurn(GamePresenter presenter){
        Tile nextTile = deck.getTile();
        if(nextTile == null){
            presenter.finishSession();
            return;
        }
        currentPlayerIdx = (currentPlayerIdx + 1) % players.length;
        players[currentPlayerIdx].doTurn(presenter, gameMap, nextTile);
        notifyObservers();
    }
    public void finish(){
        judge.countMapScores();
        notifyObservers();
        // возможно при завершении надо сделать что-то еще
    }

    public Judge judge() {
        return judge;
    }

    public void addTile(int x, int y, Tile tile) {
        try {
            gameMap.putTile(x,y,tile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        judge.countTileScores(tile,false);
        notifyObservers();
    }

    public int getCurrentPlayerIdx(){
        return currentPlayerIdx;
    }

    public Map getGameMap(){
        return gameMap;
    }

    @Override
    public void addObserver(ru.nsu.ccfit.chumak.util.Observer<Player> observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Player> observer : observers) {
            observer.update(players[getCurrentPlayerIdx()]);
        }
    }

}
