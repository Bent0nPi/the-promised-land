package ru.nsu.ccfit.chumak.util.exceptions;

public class OccupiedPositionException extends Exception {
    public OccupiedPositionException(String message) {
        super(message);
    }

    public OccupiedPositionException(int x, int y) {
        super("Occupied position was used in map: " + x + ", " + y);
    }
}
