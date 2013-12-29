package com.purediscovery.vennlayout.model.geom;


import junit.framework.TestCase;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 *
 */
public class AreaUtilTest extends TestCase {

    public void test() {

        float r = 100;
        GeneralPath circle = AreaUtil.makeCircle(0, 0, r, 100);
        double area = AreaUtil.approxArea(circle);

        double expectedArea = Math.PI * r * r;

        assertAproxEqual("area", area, expectedArea);


        double perimeter = AreaUtil.perimeter(circle);

        double expectedPrim = Math.PI * r * 2;

        assertAproxEqual("perimeter", perimeter, expectedPrim);

    }


    public void test2() {

        float r = 100;
        GeneralPath square = AreaUtil.makeCircle(0, 0, r, 4);
        double area = AreaUtil.approxArea(square);

        double side = r / Math.sqrt(2) * 2;
        double expectedArea = side * side;

        assertAproxEqual("area", area, expectedArea);

    }


    public void testIntersect() {

        long start = System.nanoTime();

        float r = 100;
        GeneralPath sq1 = AreaUtil.makeCircle(0, 0, r, 4);
        GeneralPath sq2 = AreaUtil.makeCircle(r, 0, r, 4);

        Area a1 = new Area(sq1);
        Area a2 = new Area(sq2);

        a1.intersect(a2);
        double area = AreaUtil.approxArea(a1);

        double side = r / Math.sqrt(2);
        double expectedArea = side * side;

        assertAproxEqual("area", area, expectedArea);


        long stop = System.nanoTime();
        System.out.println("time=" + (stop - start) / 1e6 + " ms");

    }


    public void testSubtract() {

        long start = System.nanoTime();

        float r = 100;
        GeneralPath sq1 = AreaUtil.makeCircle(0, 0, r, 4);
        GeneralPath sq2 = AreaUtil.makeCircle(r, 0, r, 4);

        Area a1 = new Area(sq1);
        Area a2 = new Area(sq2);

        a1.subtract(a2);
        double area = AreaUtil.approxArea(a1);

        double side = r * Math.sqrt(2);
        double halfSide = side / 2;
        double expectedArea = side * side - halfSide * halfSide;

        assertAproxEqual("subtract area", area, expectedArea);


        long stop = System.nanoTime();
        System.out.println("time=" + (stop - start) / 1e6 + " ms");

    }


    public void testInternal() {

        long start = System.nanoTime();

        float r = 100;
        GeneralPath sq1 = AreaUtil.makeCircle(0, 0, r, 4);
        GeneralPath sq2 = AreaUtil.makeCircle(0, 0, r / 2, 4);

        Area a1 = new Area(sq1);
        Area a2 = new Area(sq2);

        a1.subtract(a2);
        double area = AreaUtil.approxArea(a1);

        double side = r * Math.sqrt(2);
        double halfSide = side / 2;
        double expectedArea = side * side - halfSide * halfSide;

        assertAproxEqual("internal subtract area", area, expectedArea);


        long stop = System.nanoTime();
        System.out.println("time=" + (stop - start) / 1e6 + " ms");

        double perimeter = AreaUtil.perimeter(a1);
        double expectedPerimeter = side * 4 + halfSide *4;

        assertAproxEqual("internal subtract perimeter", perimeter, expectedPerimeter);


    }



    public void testCenter(){
        GeneralPath p = new GeneralPath();

        double expectedX = 1;
        double expectedY = 2;

        double r = 2;

        p.moveTo(expectedX +0, expectedY +r);
        p.lineTo(expectedX +r, expectedY +0);
        p.lineTo(expectedX -r, expectedY +0);
        p.lineTo(expectedX +0, expectedY -r);
        p.closePath();

        Point2D.Double result = AreaUtil.center(p.getPathIterator(new AffineTransform()));

        assertAproxEqual("center x", result.x, expectedX);
        assertAproxEqual("center y", result.y, expectedY);

        double aveRadius = AreaUtil.averageRadius(p);
        assertAproxEqual("ave radius", r, aveRadius);


    }





    private static void assertAproxEqual(String name, double actual, double expected) {
        System.out.println("expected " + name + " = " + expected);
        System.out.println("actual " + name + " = " + actual);
        assertTrue(Math.abs(actual - expected) / expected < 0.01f);
    }

}
