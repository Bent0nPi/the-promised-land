package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import ru.nsu.ccfit.chumak.model.Building;
import ru.nsu.ccfit.chumak.model.Worker;
import ru.nsu.ccfit.chumak.util.GameUtil;
import ru.nsu.ccfit.chumak.util.Observer;
import ru.nsu.ccfit.chumak.view.controllers.Mouse;

public class WorkerView implements Observer<Worker> {

    private Entity workerEntity;

    private int timeToLive = 14*6;
    private boolean isLive = true;
    private SpriteSheet dead = new SpriteSheet("/graphics/units/Dead.png", 128, 128);

    private Point2D lastCameraPosition;
    private boolean isReal;

    public WorkerView(Worker worker, int index, Building building, Point2D cameraPosition, boolean isReal) {
        if(building == null) {
            workerEntity = new Entity(new SpriteSheet("/graphics/units/Pawn_"+ GameUtil.getColor(index)+".png", 192, 192),new Point2D(worker.getPosition().getX() - 81-cameraPosition.getX(), worker.getPosition().getY()- 81-cameraPosition.getY()));
            dead = new SpriteSheet("/graphics/units/Foam.png", 192, 192);
            timeToLive = 4*6;
        } else{
            if(building.getShapes().getFirst().getTag().equals("TOWN")){
                workerEntity = new Entity(new SpriteSheet("/graphics/units/Warrior_"+ GameUtil.getColor(index)+".png", 192, 192),4,new Point2D(worker.getPosition().getX()- 81 -cameraPosition.getX(), worker.getPosition().getY() - 81-cameraPosition.getY()));
            } else if(building.getShapes().get(0).getTag().equals("ROAD")){
                workerEntity = new Entity(new SpriteSheet("/graphics/units/Archer_"+ GameUtil.getColor(index)+".png", 192, 192),4,new Point2D(worker.getPosition().getX()- 81 -cameraPosition.getX(), worker.getPosition().getY()- 81 -cameraPosition.getY()));
            } else {
                workerEntity = new Entity(new SpriteSheet("/graphics/units/Pawn_"+ GameUtil.getColor(index)+".png", 192, 192),2,new Point2D(worker.getPosition().getX() - 81-cameraPosition.getX(), worker.getPosition().getY()- 81-cameraPosition.getY()));
            }
        }
        worker.addObserver(this);
        lastCameraPosition = cameraPosition;
        this.isReal = isReal;
    }

    public ImageView getView() {
        return workerEntity.getView();
    }

    @Override
    public void update(Worker worker) {
        workerEntity.setPosition(new Point2D(worker.getPosition().getX()-81-lastCameraPosition.getX(),worker.getPosition().getY()- 81 -lastCameraPosition.getY()));
        if(!worker.isActive() && isLive && isReal) {
            workerEntity.setSprites(dead);
            isLive = false;
            workerEntity.setAnimation(new Animation(dead.getRow(0), true, true));
            workerEntity.setPosition(new Point2D(worker.getPosition().getX() - lastCameraPosition.getX()-17, worker.getPosition().getY()-17 - lastCameraPosition.getY()));
            workerEntity.getView().resize(128, 128);
        }
    }

    public void timeUpdate(Point2D cameraPosition){
        workerEntity.setPosition(new Point2D(workerEntity.getPosition().getX()-cameraPosition.getX() + lastCameraPosition.getX(),workerEntity.getPosition().getY() -cameraPosition.getY() + lastCameraPosition.getY()));
        lastCameraPosition = lastCameraPosition.add(cameraPosition.getX() - lastCameraPosition.getX(),cameraPosition.getY() - lastCameraPosition.getY());
        if(!isLive && timeToLive > 0){
            timeToLive--;
            workerEntity.update();
        } else if (isLive){
            workerEntity.update();
        } else{
            workerEntity.getView().setVisible(false);
        }
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public Entity getWorkerEntity() {
        return workerEntity;
    }

    public void playDead(){
        isLive = false;
        workerEntity.setSprites(dead);
        workerEntity.getView().resize(128,128);
        workerEntity.setAnimation(new Animation(dead.getRow(0),6, true, true));
    }

}
