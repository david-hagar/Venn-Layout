package com.purediscovery.vennlayout.model.mutators;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.awt.geom.GeneralPath;

/**
 *
 */
public class Bulge extends AbstractMutator {
    int length;
    int i = 0;
     private double r = 0;

    @Override
    public GeneralPath mutate(final GeneralPath generalPath, double mutationScale) {
        length = AreaUtil.getClosedPathLength(generalPath);
        i = 0;

        double aveRadius = AreaUtil.averageRadius(generalPath);
        //r = Math.random() * aveRadius / 2;
        r= aveRadius/2   ;

        return super.mutate(generalPath, mutationScale);
    }


    @Override
    void transform(double[] doubles) {
        double angle = i++ / (double) length * Math.PI *4;
        doubles[0] += Math.cos(angle) * r;
        doubles[1] += Math.sin(angle) * r;
    }
}
