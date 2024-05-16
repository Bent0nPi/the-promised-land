package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.util.Observable;
import ru.nsu.ccfit.chumak.util.Observer;
import ru.nsu.ccfit.chumak.util.exceptions.IncompatibleTileException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tile implements Observable<Tile> {

    private final static Logger logger = LogManager.getLogger(Tile.class);

    private final ArrayList<Observer<Tile>> observers = new ArrayList<>();
    @JsonProperty("shapes")
    private Shape[] shapes;
    @JsonProperty("specialPoints")
    private Point[] specialPoints;
    @JsonProperty("basePoint")
    private Point basePoint;
    @JsonProperty("buildings")
    private Building[] buildings;
    @JsonProperty("resource")
    private String resource;
    private double angle;

    @JsonCreator
    public Tile(@JsonProperty("shapes")Shape[] shapes,@JsonProperty("specialPoints")Point[] specialPoints, @JsonProperty("buildings")Building[] buildings, @JsonProperty("basePoint")Point basePoint, @JsonProperty("resource")String resource) {
        ArrayList<Shape> tmpShapes = new ArrayList<>(List.of(shapes));
        ArrayList<Point> tmpSpecialPoints = new ArrayList<>(List.of(specialPoints));
        this.specialPoints = specialPoints;
        this.basePoint = basePoint;
        this.angle = 0;
        this.buildings = buildings;
        for(Building building : buildings) {
            for(Shape shape : building.getShapes()) {
                if(!tmpShapes.contains(shape)) {
                    tmpShapes.add(shape);
                }
            }
        }
        this.shapes = tmpShapes.toArray(new Shape[0]);
        for(Building building : buildings) {
            if(!tmpSpecialPoints.contains(building.getWorkPoint())) {
                tmpSpecialPoints.add(building.getWorkPoint());
            }
        }
        this.specialPoints = tmpSpecialPoints.toArray(new Point[0]);
        this.resource = resource;
    }

    public Tile(String resource, Point basePoint) {
        this.resource = resource;
        this.basePoint = basePoint;
        this.shapes = new Shape[0];
        this.specialPoints = new Point[0];
        this.buildings = new Building[0];
    }

    public void rotate(double angle) {
        for (Shape shape : shapes) {
            shape.rotate(angle);
        }
        for (Point point : specialPoints) {
            point.rotate(angle, new Point(0,0));
        }
        this.angle = (this.angle + angle) % 360;
        notifyObservers();
    }

    public double getAngle() {
        return angle;
    }

    public Point getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(Point basePoint) {
        this.basePoint = basePoint;
        for(Shape shape : shapes) {

            shape.setBasePoint(basePoint);
        }
        notifyObservers();
    }

    public Building[] getBuildings() {
        return buildings;
    }

    public boolean canJoin(Tile tile){
        logger.info("check if " + this + " can join " + tile);
        for (Shape shape : shapes) {
            for (Shape otherShape : tile.shapes) {
                if(shape.isTouched(otherShape) && !shape.checkCompatibility(otherShape)){
                    logger.info(this + " can't join " + tile + "because of shapes " + shape + " and " + otherShape);
                    return false;
                }
            }
        }
        logger.info(this + " can join " + tile);
        return true;
    }

    public String getResource() {
        return resource;
    }

    public void join(Tile tile) throws IncompatibleTileException {
        for (Shape shape : shapes) {
            for (Shape otherShape : tile.shapes) {
                if(shape.isTouched(otherShape) && !shape.checkCompatibility(otherShape)){
                    throw new IncompatibleTileException(tile.basePoint.getX(), tile.basePoint.getY());
                }
                if(shape.isTouched(otherShape)){
                    shape.join(otherShape);
                    otherShape.join(shape);

                    Building oldBuilding = findBuilding(shape);
                    Building newBuilding = tile.findBuilding(otherShape);
                    if (oldBuilding != null && newBuilding != null) {
                        oldBuilding.join(newBuilding);
                        for (int i = 0 ; i < tile.buildings.length ; i++) {
                            if (tile.buildings[i] == newBuilding) {
                                tile.buildings[i] = oldBuilding;
                            }
                        }
                    } else if(oldBuilding == null && newBuilding != null){
                        newBuilding.getShapes().add(shape);
                    } else if (oldBuilding != null){
                        oldBuilding.getShapes().add(otherShape);
                    }
                }
            }
        }
    }

    private Building findBuilding(Shape shape) {
        for (Building building : buildings) {
            if (building.getShapes().contains(shape)) {
                return building;
            }
        }
        return null;
    }


    @Override
    public void addObserver(Observer<Tile> observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer<Tile> observer: observers){
            observer.update(this);
        }
    }

    public Point[] getSpecialPoints() {
        return specialPoints;
    }

    public Shape[] getShapes() {
        return shapes;
    }
}
