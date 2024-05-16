package ru.nsu.ccfit.chumak.view.graphics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import ru.nsu.ccfit.chumak.model.GameModel;
import ru.nsu.ccfit.chumak.model.Player;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;

import java.awt.*;
import java.util.Objects;

public class ResultsPane extends StackPane {
    GamePresenter presenter;

    public ResultsPane(Player[] results, double width, double height, GamePresenter presenter) {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/score_board.png")).toExternalForm());
        Image startBtnImg = new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/start_btn.png")).toExternalForm());
        Image pressedStartBtnImg = new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/start_btn_pressed.png")).toExternalForm());
        VBox box = new VBox();
        this.presenter = presenter;
        box.setAlignment(Pos.CENTER);
        setWidth(backgroundImage.getWidth());
        setHeight(backgroundImage.getHeight());
        Label title = new Label("Results");
        title.setStyle("-fx-font-weight: bold");
        title.setStyle("-fx-font-size: 48px;");
        title.getStyleClass().add("title");
        box.getChildren().add(title);
        for(Player player : results) {
            Label playerResult = new Label(player.getName() + " : " + player.getScore());
            playerResult.setStyle("-fx-font-weight: bold");
            playerResult.setStyle("-fx-font-size: 36px;");
            playerResult.setAlignment(Pos.CENTER);
            box.getChildren().add(playerResult);
        }
        ImageView refreshButton = new ImageView(startBtnImg);
        refreshButton.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override
        public void handle(MouseEvent mouseEvent) {
            refreshButton.setImage(pressedStartBtnImg);
            presenter.restart();
        }});
        box.setSpacing(25);
        box.getChildren().add(refreshButton);
        getChildren().addAll(new ImageView(backgroundImage), box);

    }
}
