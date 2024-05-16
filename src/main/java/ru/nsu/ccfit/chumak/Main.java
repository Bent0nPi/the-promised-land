package ru.nsu.ccfit.chumak;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.model.GameModel;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;
import ru.nsu.ccfit.chumak.view.config.StageManger;
import ru.nsu.ccfit.chumak.view.graphics.GamePane;
import ru.nsu.ccfit.chumak.view.graphics.GameScene;

public class Main extends Application {

    private final static Logger logger = LogManager.getLogger(Main.class);

    private StageManger stageManger;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GamePresenter presenter = new GamePresenter();
        logger.info("Created presenter");

        stageManger = new StageManger();
        stage.setTitle("The Promised Land");
        GamePane mainPane = new GamePane(stageManger.getStageWidth(), stageManger.getStageHeight(), presenter);
        logger.info("Created main game pain");
        GameModel gameModel = new GameModel(presenter);
        logger.info("Created game model");
        stage.setScene(new GameScene(stageManger.getStageWidth(), stageManger.getStageHeight(),mainPane));
        stage.show();
        logger.info("Show stage");
    }
}