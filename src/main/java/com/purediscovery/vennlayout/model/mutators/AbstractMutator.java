package com.purediscovery.vennlayout.model.mutators;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 *
 */
public abstract class AbstractMutator implements Mutator {
    double[] tmpDoubles = new double[6];

    public GeneralPath mutate(final GeneralPath generalPath, double mutationScale) {

        PathIterator pathIterator = new PathIterator() {

            private PathIterator source = generalPath.getPathIterator(new AffineTransform());

            public int getWindingRule() {
                return source.getWindingRule();
            }

            public boolean isDone() {
                return source.isDone();
            }

            public void next() {
                source.next();
            }

            public int currentSegment(float[] floats) {
                int ret = currentSegment(tmpDoubles);
                for (int i = 0; i < floats.length; i++)
                    floats[i] = (float) tmpDoubles[i];

                return ret;
            }

            public int currentSegment(double[] doubles) {
                int ret = source.currentSegment(doubles);
                transform(doubles);
                return ret;
            }
        };


        GeneralPath p = new GeneralPath();
        p.append(pathIterator, false);
        return p; 
    }


    abstract void transform(double[] doubles);


    


}