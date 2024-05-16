package ru.nsu.ccfit.chumak.util;

public interface Observable<T> {
    void addObserver(Observer<T> observer);
    void notifyObservers();
}
