package com.purediscovery.vennlayout.model.bezier;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 */
public class ContinuousBezier {

    ArrayList<ControlPoint> points;


    public ContinuousBezier() {
        this(10);
    }


    public ContinuousBezier(int capacity) {
        points = new ArrayList<ControlPoint>(capacity);
    }


    public void add(double x, double y, double cx, double cy) {
        points.add(new ControlPoint(x, y, cx, cy));
    }


    public GeneralPath toPath() {

        GeneralPath p = new GeneralPath();


        for (int i = 0; i < points.size(); i++) {
            ControlPoint ps = points.get(i);

            if (i == 0)
                p.moveTo(ps.x, ps.y);

            int nextIndex = (i + 1) % points.size();
            ControlPoint next_ps = points.get(nextIndex);
            Point2D.Double d = new Point2D.Double(next_ps.x * 2 - next_ps.cx, next_ps.y * 2 - next_ps.cy);

            p.curveTo(ps.cx, ps.cy, d.x, d.y, next_ps.x, next_ps.y);

        }


        return p;


    }


    static public ContinuousBezier makeCircle(double x, double y, double r) {
        ContinuousBezier b = new ContinuousBezier();

        //  A = (0,r) ; B = (r,0) ; A' = (r*kappa,r) ; B' = (r,r*kappa)
        double kappa = 0.5522847498;
        double rKappa = r * kappa;

        b.add(x + r, y, x + r, y + rKappa);
        b.add(x, y + r, x - rKappa, y + r);
        b.add(x - r, y, x - r, y - rKappa);
        b.add(x, y - r, x + rKappa, y - r);


        return b;
    }


    public int size() {
        return points.size();
    }

    public ArrayList<ControlPoint> getPoints() {
        return points;
    }

    public static final class ControlPoint {
        public double x;
        public double y;
        public double cx;
        public double cy;


        private ControlPoint(double x, double y, double cx, double cy) {
            this.x = x;
            this.y = y;
            this.cx = cx;
            this.cy = cy;
        }
    }


    public Point2D.Double getCenter() {

        double dx = 0;
        double dy = 0;

        for (ContinuousBezier.ControlPoint p : points) {
            dx += p.x + p.cx;
            dy += p.y + p.cy;
        }

        dx/= points.size() *2;
        dy/= points.size() *2;

        return new Point2D.Double(dx, dy);

    }


}
