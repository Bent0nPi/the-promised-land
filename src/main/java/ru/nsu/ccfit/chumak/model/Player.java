package ru.nsu.ccfit.chumak.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;

import java.util.ArrayList;

public class Player {

    private final static Logger logger = LogManager.getLogger(Player.class);

    private final String name;
    private int score;
    private ArrayList<Worker> workers;

    private final TurnStrategy turnStrategy;

    public Player(String name, int score, TurnStrategy turnStrategy) {
        this.name = name;
        this.score = score;
        this.turnStrategy = turnStrategy;
        this.workers = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            workers.add(new Worker(name,new Point(0,0)));
        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore(int increment) {
        score += increment;
    }

    public void doTurn(GamePresenter presenter, Map map, Tile tile){
        turnStrategy.doTurn(presenter, map, tile, workers);
    }

    public int getWorkersCount() {
        int counter = 0;
        for(Worker worker : workers){
            if(!worker.isActive()){
                counter++;
            }
        }
        return counter;
    }

    public void returnWorkers() {
        for(Worker worker : workers){
            worker.setActive(false);
            worker.setPosition(new Point(0,0));
        }
    }
}
