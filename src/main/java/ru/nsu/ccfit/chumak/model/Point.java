package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;

import java.util.Objects;

public class Point {

    private final static Logger logger = LogManager.getLogger(Point.class);

    @JsonProperty("x")
    private double x;
    @JsonProperty("y")
    private double y;

    @JsonCreator
    public Point(@JsonProperty("x")double x, @JsonProperty("y")double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point addVector(Point point) {
        this.x += point.getX();
        this.y += point.getY();
        logger.info("point " + this + "coords incremented to " + this.x + ", " + this.y);
        return this;
    }

    public Point subtractVector(Point point) {
        this.x -= point.getX();
        this.y -= point.getY();
        logger.info("point " + this + "coords decremented to " + this.x + ", " + this.y);
        return this;
    }

    public Point rotate(double angle, Point point) {
        subtractVector(point);
        double newX = Math.round(this.x * Math.cos(Math.toRadians(angle)) + this.y * Math.sin(Math.toRadians(angle)));
        double newY = Math.round( - this.x * Math.sin(Math.toRadians(angle)) + this.y * Math.cos(Math.toRadians(angle)));
        this.x = newX;
        this.y = newY;
        addVector(point);
        logger.info("point " + this + " was rotated on " + angle + " related on " + point.getX() + ", " + point.getY() + " and now coords are " + this.x + ", " + this.y);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
