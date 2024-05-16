package ru.nsu.ccfit.chumak.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.util.exceptions.IncompatibleTileException;
import ru.nsu.ccfit.chumak.util.exceptions.OccupiedPositionException;

import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.round;

public class Map {

    private final static Logger logger = LogManager.getLogger(Map.class);

    public static class Coords{
        private int x;
        private int y;
        public Coords(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return x == coords.x && y == coords.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    private final HashMap<Coords, Tile> map;
    private final double stepX;
    private final double stepY;

    public Map(double stepX, double stepY, Tile firstTile){
        this.stepX = stepX;
        this.stepY = stepY;
        map = new HashMap<>();
        map.put(new Coords(0,0), firstTile);
    }

    public Coords transformToCoords(double x, double y){
        return new Coords((int)Math.round(x/stepX), (int)Math.round(y/stepY));
    }

    public boolean hasNeighbours(int x, int y){
        return map.containsKey(new Coords(x-1,y)) || map.containsKey(new Coords(x+1,y)) || map.containsKey(new Coords(x,y-1)) || map.containsKey(new Coords(x,y+1));
    }

    public boolean isAvailable(int x, int y){
        return !map.containsKey(new Coords(x,y));
    }

    public boolean isCompatible(int x, int y, Tile tile){
        if(map.containsKey(new Coords(x,y))){
            return false;
        }
        Tile currentTile = map.get(new Coords(x-1,y));
        if(!(currentTile == null || currentTile.canJoin(tile))) return false;
        currentTile = map.get(new Coords(x+1,y));
        if(!(currentTile == null || currentTile.canJoin(tile))) return false;
        currentTile = map.get(new Coords(x,y-1));
        if(!(currentTile == null || currentTile.canJoin(tile))) return false;
        currentTile = map.get(new Coords(x,y+1));
        return currentTile == null || currentTile.canJoin(tile);
    }

    public void putTile(int x, int y, Tile tile) throws OccupiedPositionException, IncompatibleTileException {
        if(map.containsKey(new Coords(x,y))){
            throw new OccupiedPositionException(x,y);
        }
        if(!isCompatible(x,y,tile)){
            throw new IncompatibleTileException(x*stepX,y*stepY);
        }
        map.put(new Coords(x,y), tile);
        Tile currentTile = map.get(new Coords(x-1,y));
        if(currentTile != null)
            currentTile.join(tile);
        currentTile = map.get(new Coords(x+1,y));
        if(currentTile != null)
            currentTile.join(tile);
        currentTile = map.get(new Coords(x,y-1));
        if(currentTile != null)
            currentTile.join(tile);
        currentTile = map.get(new Coords(x,y+1));
        if(currentTile != null)
            currentTile.join(tile);
    }

    public Tile getTile(int x, int y){
        return map.get(new Coords(x,y));
    }

    public Tile getTile(Coords coords){
        return map.get(coords);
    }

    // May return null objects!!
    public Tile[] getNeighbours(int x, int y){
        return new Tile[]{map.get(new Coords(x-1,y)), map.get(new Coords(x+1,y)), map.get(new Coords(x,y-1)), map.get(new Coords(x,y+1)) };
    }

    public Tile[] getNeighbours(Tile tile){
        int x = (int) (tile.getBasePoint().getX() / stepX);
        int y = (int) (tile.getBasePoint().getY() / stepY);
        return getNeighbours(x, y);
    }

    public Tile getTile(double x, double y){
        double coordX = x / stepX;
        double coordY = y / stepY;
        return map.get(new Coords((int)round(coordX), (int)round(coordY)));
    }

    public double getStepX() {
        return stepX;
    }

    public double getStepY() {
        return stepY;
    }
}
