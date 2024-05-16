package ru.nsu.ccfit.chumak.util.exceptions;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String path) {
        super("Exception occured while accessing " + path);
    }
}
