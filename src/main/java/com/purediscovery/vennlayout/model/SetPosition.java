package com.purediscovery.vennlayout.model;

import com.purediscovery.vennlayout.model.bezier.ContinuousBezier;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 *
 */
public class SetPosition {

    private ContinuousBezier continuousBezier;
    private GeneralPath points;
    private Set set;
    private static final AffineTransform identity = new AffineTransform();


    public SetPosition(Set set, ContinuousBezier continuousBezier) {
        this.set = set;
        this.continuousBezier = continuousBezier;
        initPoints();
    }


    public GeneralPath getPoints() {
        return points;
    }

    public ContinuousBezier getContinuousBezier() {
        return continuousBezier;
    }

    public Set getSet() {
        return set;
    }


    private void initPoints() {

        GeneralPath p = continuousBezier.toPath();
        Rectangle2D b = p.getBounds2D();
        double scale = Math.max(b.getWidth(), b.getHeight());

        points = new GeneralPath();
        points.append(p.getPathIterator(identity, scale/1000), false);
    }

}
