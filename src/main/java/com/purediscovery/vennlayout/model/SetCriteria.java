package com.purediscovery.vennlayout.model;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 *
 */
public class SetCriteria {

    private String name;
    private double goalArea;
    private SetOp[] setPattern;



    public SetCriteria(String name, SetOp[] setPattern, double goalArea) {
        this.name = name;
        this.setPattern = setPattern;
        this.goalArea = goalArea;
    }

    public String getName() {
        return name;
    }

    public double getGoalArea() {
        return goalArea;
    }

    public void setGoalArea(double goalArea) {
        this.goalArea = goalArea;
    }

    public SetOp[] getSetOp() {
        return setPattern;
    }


}
