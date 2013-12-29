package com.purediscovery.vennlayout.model.bezier;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 */
public class BezierScaleMutator implements BezierMutator {
    private AffineTransform transform = new AffineTransform();


    public ContinuousBezier mutate(ContinuousBezier continuousBezier, double mutationScale) {

        ContinuousBezier newBezier = new ContinuousBezier(continuousBezier.size());


        Point2D.Double p = continuousBezier.getCenter();
        double scale = mutate(1, mutationScale);

        transform.setToIdentity();
        transform.translate(-p.x, -p.y);
        transform.scale(scale, scale);
        transform.translate(p.x / scale, p.y / scale);


        Point2D.Double p1 = new Point2D.Double();
        Point2D.Double p2 = new Point2D.Double();
        Point2D.Double p1Out = new Point2D.Double();
        Point2D.Double p2Out = new Point2D.Double();
        for (ContinuousBezier.ControlPoint cp : continuousBezier.getPoints()) {

            p1.setLocation(cp.x, cp.y);
            p2.setLocation(cp.cx, cp.cy);
            transform.transform(p1, p1Out);
            transform.transform(p2, p2Out);

            newBezier.add(p1Out.x, p1Out.y, p2Out.x, p2Out.y);
        }

        return newBezier;
    }


    private static double mutate(double value, double scale) {
        double k = Math.random() * scale - scale / 2;
        return k + value;
    }

}