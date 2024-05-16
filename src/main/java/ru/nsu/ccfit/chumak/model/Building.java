package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;
import ru.nsu.ccfit.chumak.util.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Town.class, name = "town"),
        @JsonSubTypes.Type(value = Monastery.class, name = "monastery"),
        @JsonSubTypes.Type(value = Road.class, name = "road"),
})
abstract public class Building {

    private final static Logger logger = LogManager.getLogger(Building.class);

    protected final ArrayList<Shape> shapes;
    private final ArrayList<Point> workPoints;
    private final ArrayList<Worker>  workers;
    private int score;


    protected Building() {
        this.workers = new ArrayList<>();
        this.workPoints = new ArrayList<>();
        this.shapes = new ArrayList<>();
        this.score = 0;
    }

    protected Building(Shape[] shapes, Point[] workPoints, int score) {
        this.workers = new ArrayList<>();
        this.workPoints = new ArrayList<Point>(List.of(workPoints));
        this.shapes = new ArrayList<Shape>(List.of(shapes));
        this.score = score;
    }

    protected Building(Shape[] shapes, Point[] workPoints, Worker[] workers, int score) {
        this.shapes = new ArrayList<Shape>(List.of(shapes));
        this.workPoints = new ArrayList<Point>(List.of(workPoints));
        this.workers = new ArrayList<Worker>(List.of(workers));
        this.score = score;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void join(Building building){
        shapes.addAll(building.getShapes());
        workPoints.addAll(building.workPoints);
        workers.addAll(building.workers);
        this.score += building.score;
        logger.info("Join building {} to {}", building, this);
    }

    public boolean isFinished(){
        for (Shape shape : shapes) {
            if (!shape.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public int getScore(boolean isFinish){
        return score;
    }

    public String[] getOwners(){
        if(workers.isEmpty()){
            return new String[0];
        }
        Map<String, Integer> owners = new HashMap<>();
        for (Worker worker : workers) {
            owners.put(worker.getOwner(), owners.getOrDefault(worker.getOwner(), 0) + 1);
        }
        ArrayList<String> ownerList = new ArrayList<>(owners.keySet());
        ArrayList<String> result = new ArrayList<>();
        Integer border = owners.get(ownerList.getFirst());
        for (String owner : ownerList) {
            if (owners.get(owner) > border) {
                result.clear();
                result.add(owner);
            } else if (owners.get(owner).equals(border)) {
                result.add(owner);
            }
        }
        String[] resultArray = new String[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }

    public Point getWorkPoint(){
        if(!workPoints.isEmpty()){
            return workPoints.getLast();
        }
        return null;
    }

    public void removeWorkPoints(){
        workPoints.clear();
    }

    public Point getAvailableWorkPoint(){
        if(workers.isEmpty() && !workPoints.isEmpty()){
            return workPoints.getLast();
        }
        return null;
    }

    public void addWorker(Worker worker){
        logger.info("set isActive = true for " + worker);
        worker.setActive(true);
        logger.info("add worker " + worker);
        workers.add(worker);
    }

    public void removeWorkers(){
        for (Worker worker : workers) {
            logger.info("Set isActive = false for " + worker);
            worker.setActive(false);
        }
        logger.info("clear workers ArrayList in" + this);
        workers.clear();
    }
}
