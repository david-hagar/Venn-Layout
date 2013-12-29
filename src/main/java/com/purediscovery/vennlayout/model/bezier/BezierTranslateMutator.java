package com.purediscovery.vennlayout.model.bezier;

/**
 *
 */
public class BezierTranslateMutator implements BezierMutator{


    public ContinuousBezier mutate(ContinuousBezier continuousBezier, double mutationScale) {

        ContinuousBezier newBezier = new ContinuousBezier(continuousBezier.size());


        double dx = mutate(0,mutationScale);
        double dy = mutate(0,mutationScale);

        for (ContinuousBezier.ControlPoint p : continuousBezier.getPoints()) {

            newBezier.add(p.x +dx, p.y+dy, p.cx+dx, p.cy+dy);
        }


        return newBezier;
    }


    private static double mutate(double value, double scale) {
        double k = Math.random() * scale - scale / 2;
        return k + value;
    }

}
