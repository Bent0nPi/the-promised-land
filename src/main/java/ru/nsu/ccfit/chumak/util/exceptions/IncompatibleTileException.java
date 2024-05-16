package ru.nsu.ccfit.chumak.util.exceptions;

public class IncompatibleTileException extends Exception {
    public IncompatibleTileException(String message) {
        super("Incompatible tile: " + message);
    }

    public IncompatibleTileException(double x, double y) {
        super("Incompatible tile: " + x + ", " + y);
    }
}
