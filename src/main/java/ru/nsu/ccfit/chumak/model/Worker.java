package ru.nsu.ccfit.chumak.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.util.Observable;
import ru.nsu.ccfit.chumak.util.Observer;

import java.util.ArrayList;

public class Worker implements Observable<Worker> {

    private final static Logger logger = LogManager.getLogger(Worker.class);

    private final ArrayList<Observer<Worker>> observers = new ArrayList<>();
    private final String owner;
    private Point position;
    private boolean active;

    public Worker(String owner, Point position)  {
        this.owner = owner;
        this.position = position;
        this.active = false;
    }
    public String getOwner() {
        return owner;
    }

    public Point getPosition() {
        return position;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
        notifyObservers();
    }

    public void setPosition(final Point position) {
        this.position = position;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer<Worker> observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Worker> observer : observers) {
            observer.update(this);
        }
    }
}
