package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;

public class Road extends Building{

    private final static Logger logger = LogManager.getLogger(Road.class);

    @JsonCreator
    public Road(@JsonProperty("shapes")Shape[] shapes, @JsonProperty("workPoints")Point[] workPoints){
        super(shapes, workPoints, 1);
    }
}
