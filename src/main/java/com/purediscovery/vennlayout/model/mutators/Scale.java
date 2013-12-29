package com.purediscovery.vennlayout.model.mutators;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 *
 */

public class Scale implements Mutator {
    private AffineTransform transform= new AffineTransform();


    public GeneralPath mutate(final GeneralPath generalPath, double mutationScale) {


        double scale = 1 + Math.random() * mutationScale - mutationScale / 2;

        Point2D.Double p = AreaUtil.center(generalPath);
        transform.setToIdentity();
        transform.translate(-p.x, -p.y);
        transform.scale(scale, scale);
        transform.translate(p.x / scale, p.y / scale);

        GeneralPath gp = new GeneralPath();
        gp.append(generalPath.getPathIterator(transform), false);
        return gp;
        
    }


}
