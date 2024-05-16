package ru.nsu.ccfit.chumak.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;

import java.util.ArrayList;

public class OfflineTurnStrategy implements TurnStrategy {

    private final static Logger logger = LogManager.getLogger(OfflineTurnStrategy.class);

    @Override
    public void doTurn(GamePresenter presenter, Map map, Tile tile, ArrayList<Worker> workers) {
        presenter.setNewTile(tile);
        for (Worker worker : workers) {
            if(!worker.isActive()){
                presenter.setNewWorker(worker);
                break;
            }
        }

    }
}
