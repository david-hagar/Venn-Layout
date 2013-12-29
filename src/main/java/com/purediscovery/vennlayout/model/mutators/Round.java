package com.purediscovery.vennlayout.model.mutators;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 *
 */
public class Round implements Mutator {


    public GeneralPath mutate(GeneralPath generalPath, double mutationScale) {

        ArrayList<double[]> points = AreaUtil.getPoints(generalPath);
        final int size = points.size();
        GeneralPath gp = new GeneralPath();
        double[] p = new double[2];
        for (int i = 0; i < size; i++) {
            int prev = i - 1;
            if (prev < 0) prev += size;
            int next = i + 1;
            if (next >= size) next -= size;

            for (int j = 0; j < p.length; j++)
                p[j] = (points.get(prev)[j] + points.get(next)[j]) / 2;

            if (i == 0)
                gp.moveTo(p[0], p[1]);
            else
                gp.lineTo(p[0], p[1]);
        }

        gp.closePath();

        return gp;

        //todo: preserve area

    }
}
