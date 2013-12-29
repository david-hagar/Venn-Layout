package com.purediscovery.vennlayout.model.geom;

/**
 *  From: http://stackoverflow.com/questions/2263272/how-to-calculate-the-area-of-a-java-awt-geom-area
 */


import static java.lang.Double.NaN;

import java.awt.geom.*;
import java.util.ArrayList;

public abstract class AreaUtil {


    public static double approxArea(Area area) {
        PathIterator i = area.getPathIterator(identity);
        return approxArea(i);
    }

    public static double approxArea(GeneralPath p) {
        PathIterator i = p.getPathIterator(identity);
        return approxArea(i);
    }


    public static double approxArea(PathIterator i) {
        double a = 0.0;
        double[] coords = new double[6];
        double startX = NaN, startY = NaN;
        Line2D segment = new Line2D.Double(NaN, NaN, NaN, NaN);
        while (!i.isDone()) {
            int segType = i.currentSegment(coords);
            double x = coords[0], y = coords[1];
            switch (segType) {
                case PathIterator.SEG_CLOSE:
                    segment.setLine(segment.getX2(), segment.getY2(), startX, startY);
                    a += hexArea(segment);
                    startX = startY = NaN;
                    segment.setLine(NaN, NaN, NaN, NaN);
                    break;
                case PathIterator.SEG_LINETO:
                    segment.setLine(segment.getX2(), segment.getY2(), x, y);
                    a += hexArea(segment);
                    break;
                case PathIterator.SEG_MOVETO:
                    startX = x;
                    startY = y;
                    segment.setLine(NaN, NaN, x, y);
                    break;
                default:
                    throw new IllegalArgumentException("PathIterator contains curved segments");
            }
            i.next();
        }
        if (Double.isNaN(a)) {
            throw new IllegalArgumentException("PathIterator contains an open path");
        } else {
            return 0.5 * Math.abs(a);
        }
    }

    private static double hexArea(Line2D seg) {
        return seg.getX1() * seg.getY2() - seg.getX2() * seg.getY1();
    }

    private static final AffineTransform identity = new AffineTransform();


    static public GeneralPath makeCircle(double xCenter, double yCenter, double r, int nPoints) {

        if (nPoints < 4) throw new RuntimeException("too few points. n=" + nPoints);

        GeneralPath gp = new GeneralPath();

        for (int i = 0; i < nPoints; i++) {
            double angle = i / (double) nPoints * Math.PI * 2;
            double x = r * Math.cos(angle) + xCenter;
            double y = r * Math.sin(angle) + yCenter;
            if (i == 0)
                gp.moveTo(x, y);
            else gp.lineTo(x, y);
        }

        gp.closePath();

        return gp;
    }


    public static double perimeter(GeneralPath gp) {
        return perimeter(gp.getPathIterator(identity));

    }

    public static double perimeter(Area area) {
        PathIterator i = area.getPathIterator(identity);
        return perimeter(i);
    }

    private static double perimeter(PathIterator i) {
        double perimeter = 0.0;
        double[] coords = new double[6];
        double startX = NaN, startY = NaN;
        Line2D segment = new Line2D.Double(NaN, NaN, NaN, NaN);
        while (!i.isDone()) {
            int segType = i.currentSegment(coords);
            double x = coords[0], y = coords[1];
            switch (segType) {
                case PathIterator.SEG_CLOSE:
                    segment.setLine(segment.getX2(), segment.getY2(), startX, startY);
                    perimeter += length(segment);
                    startX = startY = NaN;
                    segment.setLine(NaN, NaN, NaN, NaN);
                    break;
                case PathIterator.SEG_LINETO:
                    segment.setLine(segment.getX2(), segment.getY2(), x, y);
                    perimeter += length(segment);
                    break;
                case PathIterator.SEG_MOVETO:
                    startX = x;
                    startY = y;
                    segment.setLine(NaN, NaN, x, y);
                    break;
                default:
                    throw new IllegalArgumentException("PathIterator contains curved segments");
            }
            i.next();
        }

        if (Double.isNaN(perimeter))
            throw new IllegalArgumentException("PathIterator contains an open path");

        return perimeter;

    }

    private static double length(Line2D segment) {
        double x = segment.getX1() - segment.getX2();
        double y = segment.getY1() - segment.getY2();
        return Math.sqrt(x * x + y * y);
    }

    public static Point2D.Double center(GeneralPath generalPath) {
        return center(generalPath.getPathIterator(identity));
    }

    public static Point2D.Double center(PathIterator i) {
        double[] coords = new double[6];
        double xTotal = 0;
        double yTotal = 0;
        int count = 0;
        while (!i.isDone()) {
            int segType = i.currentSegment(coords);
            if (segType != PathIterator.SEG_CLOSE) {
                xTotal += coords[0];
                yTotal += coords[1];
                count++;
            }
            i.next();
        }

        return new Point2D.Double(xTotal / count, yTotal / count);

    }


    public static double averageRadius(GeneralPath generalPath) {

        Point2D.Double center = center(generalPath.getPathIterator(identity));
        PathIterator i = generalPath.getPathIterator(identity);

        double[] coords = new double[6];
        double rTotal = 0;
        int count = 0;
        while (!i.isDone()) {
            int segType = i.currentSegment(coords);
            if (segType != PathIterator.SEG_CLOSE) {
                double x = center.x - coords[0];
                double y = center.y - coords[1];
                rTotal += Math.sqrt(x * x + y * y);
                count++;
            }
            i.next();
        }

        return rTotal / count;


    }


    public static int getClosedPathLength(GeneralPath generalPath) {
        int c = 0;

        PathIterator p = generalPath.getPathIterator(identity);
        while (!p.isDone()) {
            c++;
            p.next();
        }

        if (c > 0)  // for closed paths
            c--;

        return c;
    }


    public static ArrayList<double[]> getPoints(GeneralPath generalPath) {

        ArrayList<double[]> list = new ArrayList<double[]>();
        PathIterator i = generalPath.getPathIterator(identity);

        while (!i.isDone()) {
            double[] coords = new double[2];
            int segType = i.currentSegment(coords);
            if (segType != PathIterator.SEG_CLOSE) {
                list.add(coords);
            }
            i.next();
        }

        return list;


    }

}
