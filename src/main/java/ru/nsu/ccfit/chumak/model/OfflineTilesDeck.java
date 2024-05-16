package ru.nsu.ccfit.chumak.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;

import java.util.ArrayList;
import java.util.Random;

public class OfflineTilesDeck implements TilesDeck{

    private final static Logger logger = LogManager.getLogger(OfflineTilesDeck.class);

    ArrayList<Tile> tiles;

    public OfflineTilesDeck(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public OfflineTilesDeck() {
        this.tiles = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() {
        return  tiles.isEmpty();
    }

    @Override
    public Tile getTile() {
        if(tiles.isEmpty()){
            return null;
        }
        Random rand = new Random();
        int newIdx = rand.nextInt(tiles.size());
        Tile result = tiles.get(newIdx);
        tiles.remove(newIdx);
        return result;
    }

    @Override
    public void addTile(Tile tile) {
        tiles.add(tile);
    }
}
