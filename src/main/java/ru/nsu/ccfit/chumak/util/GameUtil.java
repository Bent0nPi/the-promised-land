package ru.nsu.ccfit.chumak.util;

public class GameUtil {
    static private final int countTiles = 12;
    static private final String tileModelResourceDir = "src/main/resources/physics/tiles/";

    static private final double tileSizeX = 240;
    static private final double tileSizeY = 240;

    static private final double tileImageSizeX = 530;
    static private final double tileImageSizeY = 530;

    static private final String[] playerColors = {"Blue", "Purple", "Red", "Yellow"};

    public static int getCountTiles() {
        return countTiles;
    }
    public static String getTileModelResourceDir() {
        return tileModelResourceDir;
    }

    public static double getTileSizeX() {
        return tileSizeX;
    }

    public static double getTileSizeY() {
        return tileSizeY;
    }

    public static String getColor(int i){
        return playerColors[i];
    }
}
