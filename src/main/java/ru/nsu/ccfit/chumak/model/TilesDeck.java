package ru.nsu.ccfit.chumak.model;

public interface TilesDeck {
    public boolean isEmpty();
    public Tile getTile();
    public void addTile(Tile tile);
}
