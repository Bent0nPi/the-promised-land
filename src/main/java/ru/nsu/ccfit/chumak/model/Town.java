package ru.nsu.ccfit.chumak.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.chumak.Main;

import java.util.ArrayList;
import java.util.List;

public class Town extends Building{

    private final static Logger logger = LogManager.getLogger(Town.class);

    int countShields = 0;

    @JsonCreator
    public Town(@JsonProperty("shapes")Shape[] shapes, @JsonProperty("workPoints")Point[] workPoints, @JsonProperty("countShields")int countShields) {
        super(shapes, workPoints, countShields + 2);
        this.countShields = countShields;
    }

    public Town() {
        super();
    }




}
