package ru.nsu.ccfit.chumak.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;
import ru.nsu.ccfit.chumak.util.Observable;
import ru.nsu.ccfit.chumak.util.Observer;
import ru.nsu.ccfit.chumak.util.PlayerTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModel {

    private final static Logger logger = LogManager.getLogger(GameModel.class);

    private GamePresenter presenter;
    private Session session;
    private Player[] players;
    private Player[] results;


    public GameModel(GamePresenter presenter) {
        this.presenter = presenter;
        presenter.setModel(this);
    }

    public void choosePlayers(String[] playerNames, PlayerTypes[] playerTypes) {

        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            TurnStrategy turnStrategy = null;
            if(playerTypes[i] == PlayerTypes.OFFLINE){
                turnStrategy = new OfflineTurnStrategy();
            }
            players[i] = new Player(playerNames[i], 0, turnStrategy);
        }
        logger.info("Chose players: " + Arrays.toString(playerNames));
    }

    public void startSession() {
        session = new Session(players, presenter);
        logger.info("Session created");
        presenter.connectSessionInterface();
        logger.info("Interface connected");
        logger.info("Start next turn");
        session.doNextTurn(presenter);

    }

    public void doTurn() {
        session.doNextTurn(presenter);
    }

    public void finish(){
        logger.info("Start finish session");
        session.finish();
        logger.info("start sort players");
        sortPlayers();
        logger.info("show result table");
        presenter.showResults(results);
    }

    private void sortPlayers(){
        ArrayList<Player> tmpResults = new ArrayList<>(List.of(players));
        tmpResults.sort((player1, player2) -> -1 * Integer.compare(player1.getScore(),player2.getScore()));
        results = tmpResults.toArray(new Player[0]);
    }

    public int getPlayerIndex(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getName().equals(playerName)) {
                return i;
            }
        }
        return -1;
    }

    public Session getSession() {
        return session;
    }

    public void restart() {
        for (Player player : players) {
            player.returnWorkers();
            player.setScore(0);
        }
        startSession();
    }
}
