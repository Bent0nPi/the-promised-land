package ru.nsu.ccfit.chumak.view.graphics;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ru.nsu.ccfit.chumak.model.Player;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsPane extends StackPane {
    GamePresenter presenter;
    private VBox box = new VBox();
    private ArrayList<TextField> names = new ArrayList<TextField>();

    public SettingsPane(GamePresenter presenter) {

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/score_board.png")).toExternalForm());
        Image startBtnImg = new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/start_btn.png")).toExternalForm());
        Image pressedStartBtnImg = new Image(Objects.requireNonNull(getClass().getResource("/graphics/turnInterface/start_btn_pressed.png")).toExternalForm());

        this.presenter = presenter;
        box.setAlignment(Pos.CENTER);
        setWidth(backgroundImage.getWidth());
        setHeight(backgroundImage.getHeight());
        Label title = new Label("Insert player names!");
        title.setStyle("-fx-font-weight: bold");
        title.setStyle("-fx-font-size: 48px;");
        title.getStyleClass().add("title");
        box.getChildren().add(title);

        ImageView refreshButton = new ImageView(pressedStartBtnImg);

        for(int i = 0; i < 4; i++) {
            TextField playerName = new TextField();
            playerName.setMaxWidth(400);
            playerName.setAlignment(Pos.CENTER);
            box.getChildren().add(playerName);
            names.add(playerName);
            playerName.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    int count = 0;
                    for(int i = 0; i < 4; i++) {
                        if(!names.get(i).getText().isEmpty()){
                            count++;
                        }
                    }
                    if(count > 1){
                        refreshButton.setImage(startBtnImg);
                    } else{
                        refreshButton.setImage(pressedStartBtnImg);
                    }
                }
            });
        }
        refreshButton.setOnMouseClicked(new EventHandler<MouseEvent>() {@Override
        public void handle(MouseEvent mouseEvent) {
            refreshButton.setImage(pressedStartBtnImg);
            ArrayList<String> players = new ArrayList<>();
            for(int i = 0; i < 4; i++) {
                if(!names.get(i).getText().isEmpty()) {
                    players.add(names.get(i).getText());
                }
            }
            if(players.size() > 1) {
                presenter.startGame(players);
            } else {
                refreshButton.setImage(startBtnImg);
            }
        }});
        box.setSpacing(25);
        box.getChildren().add(refreshButton);
        getChildren().addAll(new ImageView(backgroundImage), box);

    }
}
