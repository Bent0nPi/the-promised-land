package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;
import java.util.Objects;


public class Shape {

    private final static Logger logger = LogManager.getLogger(Shape.class);

    @JsonProperty("vertices")
    private final Point[] vertices;
    @JsonProperty("usedEdges")
    private final int[] usedEdges; //
    @JsonProperty("basePoint")
    private Point basePoint;
    @JsonProperty("tag")
    private String tag;

    @JsonCreator
    public Shape(@JsonProperty("vertices")Point[] vertices, @JsonProperty("basePoint")Point basePoint, @JsonProperty("tag")String tag, @JsonProperty("usedEdges")int[] usedEdges) {
        this.vertices = vertices;
        this.basePoint = basePoint;
        this.usedEdges = usedEdges;
        this.tag = tag;
    }

    public Shape(Point[] vertices, Point basePoint, String tag) {
        this.vertices = vertices;
        this.basePoint = basePoint;
        this.tag = tag;
        usedEdges = new int[vertices.length];
    }

    public Point[] getVertices() {
        return Arrays.copyOf(vertices, vertices.length);
    }

    public Point getBasePoint() {
        return new Point(basePoint);
    }

    public String getTag() {
        return new String(tag);
    }

    public void setBasePoint(Point basePoint) {
        this.basePoint = basePoint;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void rotate(double angle) {
        logger.info("Start rotate shape" + this +" on " + angle);
        Point center = new Point(0,0);
        for (Point p : vertices) {
            p.rotate(angle, center);
        }
        logger.info("Finish rotate shape" + this);
    }

    public boolean checkCompatibility(Shape shape) {
        logger.info("Start check compatibility shapes" + this + ": " + this.getTag() + " and " + shape + ": " + shape.getTag());
        return shape.getTag().equals(tag);
    }

    public boolean isTouched(Shape shape) {

        logger.info("Check touching shape" + this + " with " + shape);
        if(shape == null) {
            return false;
        }

        Point neighboursBasePoint = shape.getBasePoint();
        Point[] neighboursVertices = shape.getVertices();
        for(Point neighboursPoint : neighboursVertices) {
            neighboursPoint.addVector(neighboursBasePoint);
        }
        logger.info("Neighbour points:");
        for(Point neighboursPoint : neighboursVertices) {
            logger.info("np: " + neighboursPoint.getX() + " " + neighboursPoint.getY());
        }


        Point startPoint = vertices[0];
        Point endPoint = vertices[1];

        for(int i = 0; i < vertices.length; i++) {
            if(usedEdges[i] == 1) {
                continue;
            }
            startPoint = vertices[i].addVector(basePoint);
            endPoint = vertices[(i + 1) % vertices.length].addVector(basePoint);
            logger.info("current edge: " + startPoint.getX() + " " + startPoint.getY() + ",  " + endPoint.getX() + " " + endPoint.getY());
            for(int j = 0; j < neighboursVertices.length; j++) {
                //check that edges are same
                int touchingType = checkNesting(startPoint, endPoint, neighboursVertices[j],neighboursVertices[(j+1) % neighboursVertices.length]);
                logger.info("for " +neighboursVertices[j].getX() + neighboursVertices[j].getY() + ", " + neighboursVertices[(j+1) % neighboursVertices.length].getX() + " " + neighboursVertices[(j+1) % neighboursVertices.length].getX() + " type of relation is: " + touchingType);
                if( touchingType != 0 && touchingType != -2) {
                    for(Point neighboursPoint : neighboursVertices) {
                        neighboursPoint.subtractVector(neighboursBasePoint);
                    }
                    startPoint.subtractVector(basePoint);
                    endPoint.subtractVector(basePoint);
                    logger.info("touched edge was detected");
                    return true;
                }
            }
            startPoint.subtractVector(basePoint);
            endPoint.subtractVector(basePoint);
        }

        for(Point neighboursPoint : neighboursVertices) {
            neighboursPoint.subtractVector(neighboursBasePoint);
        }
        return false;
    }

    public void join(Shape shape) {
        if(shape == null) {
            return;
        }

        Point neighboursBasePoint = shape.getBasePoint();
        Point[] neighboursVertices = shape.getVertices();
        for(Point neighboursPoint : neighboursVertices) {
            neighboursPoint.addVector(neighboursBasePoint);
        }

        Point startPoint;
        Point endPoint;

        for(int i = 0; i < vertices.length; i++) {
            if(usedEdges[i] == 1) {
                continue;
            }
            startPoint = vertices[i].addVector(basePoint);
            endPoint = vertices[(i + 1) % vertices.length].addVector(basePoint);
            for(int j = 0; j < neighboursVertices.length; j++) {
                //check that edges are same
                if (neighboursVertices[j].equals(startPoint) &&
                        neighboursVertices[(j + 1) % neighboursVertices.length].equals(endPoint) ||
                        neighboursVertices[j].equals(endPoint) &&
                                neighboursVertices[(j + 1) % neighboursVertices.length].equals(startPoint)
                ) {
                    usedEdges[i] = 1;
                    break;
                }
            }
            startPoint.subtractVector(basePoint);
            endPoint.subtractVector(basePoint);
        }
        for(Point neighboursPoint : neighboursVertices) {
            neighboursPoint.subtractVector(neighboursBasePoint);
        }

    }

    public int getCountUnfinishedEdges(){
        int count = 0;
        for(int i = 0; i < vertices.length; i++) {
            if(usedEdges[i] == 0) {
                count++;
            }
        }
        return count;
    }

    public boolean isFinished() {
        for(int i = 0; i < vertices.length; i++) {
            if(usedEdges[i] == 0) {
                return false;
            }
        }
        return true;
    }

    static public int checkNesting(Point v1, Point v2, Point u1, Point u2) { // 0 - нет пересечения -2 - касание -1 - пересечение 1 - v больше u и окружает его 2- u больше v и окружает его
        if(checkPointNesting(v1,v2,u1)){
            if(checkPointNesting(v1,v2,u2)){
                return 1;
            } else if (checkPointNesting(u1,u2,v1) || checkPointNesting(u1,u2,v2)){
                if(v1.equals(u1) || v2.equals(u1)){
                    return -2;
                }
                return -1;
            } else {
                return 0;
            }
        } else if(checkPointNesting(v1,v2,u2) && (checkPointNesting(u1,u2,v1) || checkPointNesting(u1,u2,v2))){
            if(v1.equals(u2) || v2.equals(u2)){
                return -2;
            }
            return -1;
        } else if(checkPointNesting(u1,u2,v1) && checkPointNesting(u1,u2,v2)){
            return 2;
        } else {
            return 0;
        }
    }

    static public boolean checkPointNesting(Point v1, Point v2, Point u) {
        Point vectorV = new Point(v2.getX()-v1.getX(), v2.getY()-v1.getY());
        Point vectorU = new Point(u.getX()-v1.getX(), u.getY()-v1.getY());
        if(vectorV.getX() == 0){
            if(vectorV.getY() == 0){
                return v1.equals(u);
            }
            if(vectorU.getX() == 0){
                return v1.getY() <= u.getY() && u.getY() <= v2.getY() || v2.getY() <= u.getY() && u.getY() <= v1.getY();
            } else{
                return false;
            }
        }
        if(vectorV.getY() == 0){
            if(vectorU.getX() == 0){
                return v1.equals(u);
            }
            if(vectorU.getY() == 0){
                return v1.getX() <= u.getX() && u.getX() <= v2.getX() || v2.getX() <= u.getX() && u.getX() <= v1.getX();
            } else{
                return false;
            }
        }
        if(vectorU.getX() == 0 || vectorU.getY() == 0){
            return vectorU.getX() == 0 && vectorU.getY() == 0;
        }

        double k1 = vectorV.getX() / vectorU.getX();
        double k2 = vectorV.getY() / vectorU.getY();
        if(k1 != k2) {
            return false;
        } else {
            return k1 >= 1;
        }
    }

}
