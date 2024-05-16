package ru.nsu.ccfit.chumak.model;

import ru.nsu.ccfit.chumak.presenter.GamePresenter;

import java.util.ArrayList;

public interface TurnStrategy {
    public void doTurn(GamePresenter presenter, Map map, Tile tile, ArrayList<Worker> workers);
}
