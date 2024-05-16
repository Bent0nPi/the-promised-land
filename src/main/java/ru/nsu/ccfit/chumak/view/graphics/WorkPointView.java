package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import ru.nsu.ccfit.chumak.model.Building;
import ru.nsu.ccfit.chumak.model.Point;
import ru.nsu.ccfit.chumak.util.GameUtil;
import ru.nsu.ccfit.chumak.util.Observer;

public class WorkPointView {
    private Entity workPointEntity;

    private Point2D lastCameraPosition = new Point2D(0, 0);

    private Building building;

    public WorkPointView(Building building, int playerNumber, Point2D cameraPosition, Point basePoint) {
        Point point = new Point(building.getWorkPoint().getX(),building.getWorkPoint().getY());
        point.addVector(basePoint);
        if(building.getShapes().get(0).getTag().equals("TOWN")) {
            workPointEntity = new Entity(new SpriteSheet("/graphics/units/Castle_" + GameUtil.getColor(playerNumber) + ".png"), new Point2D(point.getX() - cameraPosition.getX(), point.getY()- cameraPosition.getY()));
        } else if (building.getShapes().get(0).getTag().equals("ROAD")) {
            workPointEntity = new Entity(new SpriteSheet("/graphics/units/Tower_" + GameUtil.getColor(playerNumber) + ".png"), new Point2D(point.getX() - cameraPosition.getX(), point.getY()- cameraPosition.getY()));
        } else{
            workPointEntity = new Entity(new SpriteSheet("/graphics/units/House_" + GameUtil.getColor(playerNumber) + ".png"), new Point2D(point.getX() - cameraPosition.getX(), point.getY()- cameraPosition.getY()));
        }
        workPointEntity.setPosition(new Point2D(point.getX() - cameraPosition.getX() - workPointEntity.getSprites().getImage().getWidth()/2, point.getY() - cameraPosition.getY() - workPointEntity.getSprites().getImage().getHeight()*0.6));
        lastCameraPosition = cameraPosition;
        this.building = building;
    }

    public void timeUpdate(Point2D cameraPosition){
        workPointEntity.update();
        workPointEntity.setPosition(new Point2D(workPointEntity.getPosition().getX()-cameraPosition.getX() + lastCameraPosition.getX(),workPointEntity.getPosition().getY() -cameraPosition.getY() + lastCameraPosition.getY()));
        lastCameraPosition = lastCameraPosition.add(cameraPosition.getX() - lastCameraPosition.getX(),cameraPosition.getY() - lastCameraPosition.getY());
    }

    public ImageView getView(){
        return workPointEntity.getView();
    }

    public Entity getWorkPointEntity() {
        return workPointEntity;
    }

    public Building getBuilding() {
        return building;
    }
}
