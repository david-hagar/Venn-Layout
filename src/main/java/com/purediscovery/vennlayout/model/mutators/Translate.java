package com.purediscovery.vennlayout.model.mutators;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 *
 */
public class Translate extends AbstractMutator {
    private double dx;
    private double dy;


    @Override
    public GeneralPath mutate(final GeneralPath generalPath, double mutationScale) {

        Rectangle2D b = generalPath.getBounds2D();
        double r = Math.max(b.getWidth(), b.getHeight()) * mutationScale;
        dx = r * Math.random() - r / 2;
        dy = r * Math.random() - r / 2;

        //System.out.println("translate: " + dx + ", " + dy);
        return super.mutate(generalPath, mutationScale);
    }

    @Override
    void transform(double[] doubles) {
        doubles[0] += dx;
        doubles[1] += dy;
    }


}
