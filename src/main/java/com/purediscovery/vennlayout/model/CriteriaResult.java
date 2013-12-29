package com.purediscovery.vennlayout.model;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 *
 */
public class CriteriaResult {

    Area area = null;
    double error;

    public CriteriaResult(SetCriteria setCriteria, ArrayList<SetPosition> setPositions) throws VennException {

        area = makeArea(setCriteria, setPositions);

        double areaOfArea = (area == null) ? 0: AreaUtil.approxArea(area);

        double idealArea = setCriteria.getGoalArea();

        double areaErrorFraction = Math.abs(idealArea - areaOfArea) / idealArea;

        error = areaErrorFraction * areaErrorFraction;

    }

    private Area makeArea(SetCriteria setCriteria, ArrayList<SetPosition> setPositions) throws VennException {
        Area area = null;
        SetOp[] setOps = setCriteria.getSetOp();
        for (int i = 0; i < setOps.length; i++) {
            if (setOps[i] == SetOp.Identity) {
                GeneralPath path = setPositions.get(i).getPoints();
                if (area == null) {
                    area = new Area(path);
                } else {
                    area.intersect(new Area(path));
                    if (area.isEmpty()) return null;
                }
            }
        }

        if (area == null)
            throw new VennException("No identity set ops found.");


        for (int i = 0; i < setOps.length; i++) {
            if (setOps[i] == SetOp.Not) {
                GeneralPath path = setPositions.get(i).getPoints();

                area.subtract(new Area(path));
                if (area.isEmpty()) return null;
            }
        }

        return area;
    }


    public double getError() {
        return error;
    }

    public Area getArea() {
        return area;
    }


    public boolean isEmpty() {
        return area==null;
    }
}
