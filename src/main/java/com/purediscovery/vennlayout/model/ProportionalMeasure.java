package com.purediscovery.vennlayout.model;

import java.util.ArrayList;

/**
 *
 */
public class ProportionalMeasure implements SetMeasure {

    ArrayList<Set> setList = new ArrayList<Set>();
    int max = 100;

    public ProportionalMeasure(ArrayList<Set> setList) {
        this.setList = setList;
    }


    public int getSetCount(SetOp[] setOps) {

        int count = 0;
        for (SetOp setOp : setOps) {
            if (setOp == SetOp.Identity)
                count++;
        }


        return max / (setOps.length - count+1);
    }
}
