package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ru.nsu.ccfit.chumak.presenter.GamePresenter;
import ru.nsu.ccfit.chumak.view.controllers.Keyboard;
import ru.nsu.ccfit.chumak.view.controllers.Mouse;

public class GameScene extends Scene {
    public GameScene(int width, int height, GamePane mainPane)  {
        super(mainPane, width, height);
        this.addEventFilter(KeyEvent.KEY_PRESSED, (e) -> {
            Keyboard.down[e.getCode().getCode()] = true;
        });
        this.addEventFilter(KeyEvent.KEY_RELEASED, (e) -> {
            Keyboard.down[e.getCode().getCode()] = false;
        });

        this.addEventFilter(MouseEvent.MOUSE_RELEASED, (e) -> {
            Mouse.addClick(new Point2D(e.getSceneX(), e.getSceneY()));
            if(e.getButton().equals(MouseButton.SECONDARY) && mainPane.pickedTile != null) {
                mainPane.presenter.returnTile();
            }
            if(e.getButton().equals(MouseButton.SECONDARY) && mainPane.pickedWorker != null){
                mainPane.presenter.returnWorker();
            }
            if(e.getButton().equals(MouseButton.PRIMARY) && mainPane.pickedTile != null){
                mainPane.presenter.placeTile();
            }
            if(e.getButton().equals(MouseButton.PRIMARY) && mainPane.pickedWorker != null){
                mainPane.presenter.placeWorker();
            }
        });

        this.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            Mouse.last = new Point2D(e.getSceneX(), e.getSceneY());
        });

        this.addEventFilter(ScrollEvent.SCROLL, e ->{
            Mouse.scrollDirection = e.getDeltaY();

        });
        this.addEventFilter(ScrollEvent.SCROLL_FINISHED, e ->{
            Mouse.scrollDirection = 0;
        });
    }
}
