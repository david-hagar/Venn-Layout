package com.purediscovery.vennlayout.model.bezier;

/**
 *
 */
public class BezierDistortMutator implements BezierMutator{


    public ContinuousBezier mutate(ContinuousBezier continuousBezier, double mutationScale) {

        ContinuousBezier newBezier = new ContinuousBezier(continuousBezier.size());


        for (ContinuousBezier.ControlPoint p : continuousBezier.getPoints()) {

            newBezier.add(mutate(p.x, mutationScale), mutate(p.y, mutationScale), mutate(p.cx, mutationScale), mutate(p.cy, mutationScale));
        }


        return newBezier;
    }


    private static double mutate(double value, double scale) {
        double k = Math.random() * scale - scale / 2;
        return k + value;
    }

}
