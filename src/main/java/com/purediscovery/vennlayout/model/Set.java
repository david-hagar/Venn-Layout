package com.purediscovery.vennlayout.model;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 *
  */
public class Set {

    private String name;
    private Color color;
    private SetCriteria criteria;


    public Set(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public SetCriteria getSetCriteria() {
        return criteria;
    }

    public void setCriteria(SetCriteria criteria) {
        this.criteria = criteria;
    }
}
