package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Monastery extends Building{

    private final Shape[] controlledShapes;

    @JsonCreator
    public Monastery(@JsonProperty("shapes")Shape[] shapes, @JsonProperty("workPoints")Point[] workPoints){
        super(shapes, workPoints, 1);
        controlledShapes = new Shape[shapes.length-1];
        for (int i = 0; i < controlledShapes.length; i++){
            controlledShapes[i] = shapes[i+1];
        }
    }


    @Override
    public boolean isFinished(){
        for (Shape shape : controlledShapes) {
            if (!shape.isFinished()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getScore(boolean isFinished){
        int score = 1;
        for (Shape shape : controlledShapes) {
            score += 2 * ( shape.getVertices().length - shape.getCountUnfinishedEdges());
        }
        return score;
    }


}
