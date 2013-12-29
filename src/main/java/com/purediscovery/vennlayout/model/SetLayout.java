package com.purediscovery.vennlayout.model;

import com.purediscovery.vennlayout.model.geom.AreaUtil;

import java.util.ArrayList;

/**
 *
 */
public class SetLayout {


    ArrayList<CriteriaResult> criteriaResults = new ArrayList<CriteriaResult>();
    private ArrayList<SetPosition> setPositions;
    private double error = 0;
    private double mutationScale;
    private int zeroAreaCount;

    public SetLayout(ArrayList<SetPosition> setPositions, ArrayList<SetCriteria> setCriteriaList, double mutationScale) throws VennException {
        this.setPositions = setPositions;
        this.mutationScale = mutationScale;

        zeroAreaCount =0;
        for (SetCriteria setCriteria : setCriteriaList) {
            CriteriaResult criteriaResult = new CriteriaResult(setCriteria, setPositions);
            criteriaResults.add(criteriaResult);
            error += criteriaResult.getError();
            if( criteriaResult.getArea() == null)
                zeroAreaCount++;
        }

        double roundnessError = calcRoundnessError(setPositions);

        error+=roundnessError/100;

        error*=(zeroAreaCount+1);
    }

    private double calcRoundnessError(ArrayList<SetPosition> setPositions) {
        double error = 0;

        for (SetPosition setPosition : setPositions) {

            double perimeter =  AreaUtil.perimeter(setPosition.getPoints());

            double idealArea = setPosition.getSet().getSetCriteria().getGoalArea();
            double idealRadius = Math.sqrt(idealArea / Math.PI);
            double idealPerimeter = Math.PI * 2 * idealRadius;


            double perimeterErrorFraction = Math.abs(idealPerimeter - perimeter) / idealPerimeter;
            error+=perimeterErrorFraction;
        }


        return error;
    }


    public ArrayList<SetPosition> getSetPositions() {
        return setPositions;
    }

    public double getError() {
        return error;
    }

    public ArrayList<CriteriaResult> getCriteriaResults() {
        return criteriaResults;
    }

    public double getMutationScale() {
        return mutationScale;
    }

    @Override
    public String toString() {
        return String.format("%.6f %.3e", error, mutationScale);
    }

    public int getZeroAreaCount() {
        return zeroAreaCount;
    }
}
