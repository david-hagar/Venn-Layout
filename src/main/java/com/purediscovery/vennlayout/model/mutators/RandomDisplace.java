package com.purediscovery.vennlayout.model.mutators;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 */
public class RandomDisplace extends AbstractMutator {
    int indexToChange;
    private Point2D.Double center;
    int currentIndex;
    private double r;


    @Override
    public GeneralPath mutate(final GeneralPath generalPath, double mutationScale) {

        Rectangle2D b = generalPath.getBounds2D();
        r = Math.max(b.getWidth(), b.getHeight()) * mutationScale/20;

        center = AreaUtil.center(generalPath);

        currentIndex =0;
        indexToChange = (int)(Math.random() * AreaUtil.getClosedPathLength(generalPath));

        return super.mutate(generalPath, mutationScale);
    }

    @Override
    void transform(double[] doubles) {

        if( currentIndex++ == indexToChange){
            double dx = doubles[0] - center.x;
            double dy = doubles[1] - center.y;
            double mag = Math.sqrt( dx *dx + dy*dy);

        doubles[0] += dx/mag*r;
        doubles[1] += dy/mag*r;
        }
    }


}
