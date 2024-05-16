package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import ru.nsu.ccfit.chumak.model.Tile;
import ru.nsu.ccfit.chumak.util.Observer;

public class TileView implements Observer<Tile> {

    private final Entity tileEntity;
    private Point2D lastCameraPosition;

    public TileView(Tile tile, Point2D cameraPosition) {
        tileEntity = new Entity(new SpriteSheet(tile.getResource()), new Point2D(tile.getBasePoint().getX() - 120 - cameraPosition.getX(), tile.getBasePoint().getY() - 120 - cameraPosition.getY()));
        tile.addObserver(this);
        lastCameraPosition = cameraPosition;
    }

    public ImageView getView() {
        return tileEntity.getView();
    }

    @Override
    public void update(Tile tile) {
        tileEntity.setPosition(new Point2D(tile.getBasePoint().getX() - 120  - lastCameraPosition.getX(), tile.getBasePoint().getY() - 120 - lastCameraPosition.getY()));
        tileEntity.rotateView(tile.getAngle() - tileEntity.getAngle());
    }

    public void timeUpdate(Point2D cameraPosition){
        tileEntity.setPosition(new Point2D(tileEntity.getPosition().getX()-cameraPosition.getX() + lastCameraPosition.getX(),tileEntity.getPosition().getY() -cameraPosition.getY() + lastCameraPosition.getY()));
        lastCameraPosition = lastCameraPosition.add(cameraPosition.getX() - lastCameraPosition.getX(),cameraPosition.getY() - lastCameraPosition.getY());
        tileEntity.update();
    }

    public Entity getTileEntity() {
        return tileEntity;
    }
}
