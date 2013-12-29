package com.purediscovery.vennlayout.model.mutators;

import java.awt.geom.GeneralPath;

/**
 *
 */
public interface Mutator {


    GeneralPath mutate(GeneralPath generalPath, double mutationScale);

}
