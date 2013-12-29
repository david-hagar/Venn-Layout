package com.purediscovery.vennlayout.model;

import com.purediscovery.vennlayout.model.bezier.*;
import com.purediscovery.vennlayout.model.mutators.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 */
public class GALayout {

    static final int POPULATION_SIZE = 50;

    ArrayList<Set> setList = new ArrayList<Set>();
    ArrayList<SetCriteria> setCriteriaList = new ArrayList<SetCriteria>();
    SetMeasure setMeasure;

    ArrayList<SetLayout> population = new ArrayList<SetLayout>();
    ArrayList<BezierMutator> mutators = new ArrayList<BezierMutator>();

    Thread runThread;
    private int generationCount = 0;

    public GALayout(ArrayList<Set> setList, SetMeasure setMeasure) throws VennException {
        this.setList = setList;
        this.setMeasure = setMeasure;

        initCriteria();
        initColors();
        initFirstPosition();
        initMutators();
    }

    private void initMutators() {

        mutators.add(new BezierDistortMutator());
        mutators.add(new BezierScaleMutator());
        mutators.add(new BezierTranslateMutator());

    }

    private void initColors() {
        int i = 0;
        for (Set set : setList) {
            float hue = i++ / (float) setList.size();
            Color c = Color.getHSBColor(hue, 1.0f, 1.0f);
            set.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 75));
        }
    }


    public void initCriteria() throws VennException {

        SetOp setOps[] = new SetOp[setList.size()];
        Arrays.fill(setOps, SetOp.Identity);  // not needed but do it anyway

        traverse(setOps, 0);

        checkSetsHaveCriteria();
    }

    private void checkSetsHaveCriteria() throws VennException {

        for (Set set : setList) {
            if (set.getSetCriteria() == null)
                throw new VennException("Not all sets have criteria.");
        }
    }


    private void traverse(SetOp setOps[], int index) {
        SetOp copy[] = Arrays.copyOf(setOps, setOps.length);
        if (index >= setOps.length) {
            createCriteria(copy);
            return;
        }

        copy[index] = SetOp.Identity;
        traverse(copy, index + 1);

        copy[index] = SetOp.Not;
        traverse(copy, index + 1);
    }


    private void createCriteria(SetOp[] setOps) {

        if (isAllInverse(setOps))
            return;

        int count = setMeasure.getSetCount(setOps);

        SetCriteria criteria = new SetCriteria(makeName(setOps), setOps, countToArea(count));
        setCriteriaList.add(criteria);

        int singleSetIndex = getSingleSetIndex(setOps);
        if (singleSetIndex != -1)
            setList.get(singleSetIndex).setCriteria(criteria);
    }

    private int getSingleSetIndex(SetOp[] criteria) {
        boolean found = false;
        int index = -1;
        for (int i = 0; i < criteria.length; i++) {
            if (criteria[i] == SetOp.Identity) {
                if (found)
                    return -1;
                else {
                    index = i;
                    found = true;
                }
            }
        }

        return index;
    }


    private String makeName(SetOp[] setOps) {
        StringBuilder sb = new StringBuilder(30);
        for (int i = 0; i < setOps.length; i++) {
            sb.append(setList.get(i).getName());
            if (setOps[i] == SetOp.Not)
                sb.append('\'');
        }
        return sb.toString();
    }


    private double countToArea(int count) {
        return count;
    }


    private boolean isAllInverse(SetOp[] setOps) {

        for (SetOp setOp : setOps) {
            if (setOp == SetOp.Identity)
                return false;
        }

        return true;
    }


    public void initFirstPosition() throws VennException {

        ArrayList<SetPosition> setPositions = new ArrayList<SetPosition>();
        for (Set set : setList) {
            double r = Math.sqrt(getTotalArea(set) / Math.PI);

            double angle = setPositions.size()/(double)setList.size() * Math.PI *2;
            double centerR = r / 2;
            double x = centerR * Math.cos(angle);
            double y = centerR * Math.sin(angle);
            //double perimeter = Math.PI * r * 2;

            //int np = Math.max((int) (perimeter / .3f), 20);
            //GeneralPath gp = AreaUtil.makeCircle(x, y, r, np);
            //gp = new Round().mutate(gp, 1);
            setPositions.add(new SetPosition(set, ContinuousBezier.makeCircle(x,y,r)));
        }

        population.add(new SetLayout(setPositions, setCriteriaList, 0.1));
    }


    public double getTotalArea( Set set) throws VennException {

        int index=-1;
        for( int i=0;i<setList.size();i++)
            if( setList.get(i) == set) {
                index = i;
                break;
            }

        if( index == -1) throw new VennException("couldn't find set.");


        double area = 0;
        for (SetCriteria setCriteria : setCriteriaList) {
            if( setCriteria.getSetOp()[index] == SetOp.Identity)
                area+=setCriteria.getGoalArea();
        }

        return area;

    }


    public ArrayList<SetCriteria> getSetCriteriaList() {
        return setCriteriaList;
    }

    public ArrayList<SetLayout> getPopulation() {
        return population;
    }

    public SetLayout getBest() {
        synchronized (population) {
            return population.get(0);
        }
    }

    public ArrayList<Set> getSetList() {
        return setList;
    }


    public void start() {

        runThread = new Thread(new Runnable() {
            public void run() {
                try {
                    mainLoop();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        runThread.setName("Run Thread");
        runThread.setPriority(Thread.MIN_PRIORITY);
        runThread.start();
    }

    private void mainLoop() throws VennException {
        while (!Thread.currentThread().isInterrupted() || getBest().getError() < 0.01) {

            addNewLayout();
        }
        System.out.println("main loop exiting.");
    }

    public void stop() {
        if (runThread != null) runThread.interrupt();
    }

    public boolean isRunning() {
        return runThread != null && runThread.isAlive();
    }


    public void addNewLayout() throws VennException {
        synchronized (population) {

            BezierMutator m = mutators.get(randomIndex(mutators));
            SetLayout setLayout = population.get(randomIndex(population));
            ArrayList<SetPosition> p = new ArrayList<SetPosition>(setLayout.getSetPositions());
            int spIndex = randomIndex(p);
            SetPosition sp = p.get(spIndex);
            
            ContinuousBezier bezier = m.mutate(sp.getContinuousBezier(), setLayout.getMutationScale());
            p.set(spIndex, new SetPosition(sp.getSet(), bezier));

            SetLayout newSetLayout = new SetLayout(p, setCriteriaList, randomScale(setLayout.getMutationScale(), 0.1));

            population.add(newSetLayout);

            Collections.sort(population, new Comparator<SetLayout>() {
                public int compare(SetLayout setLayout1, SetLayout setLayout2) {
                    return Double.compare(setLayout1.getError(), setLayout2.getError());
                }
            });

            if (population.size() > POPULATION_SIZE)
                population.remove(population.size() - 1);

            generationCount++;
        }
    }

    private double randomScale(double value, double fraction) {
        double scale = 1 + Math.random() * fraction - fraction / 2;
        return value * scale;
    }

    private static int randomIndex(ArrayList mutators) {
        return (int) (Math.random() * mutators.size());
    }


    public int getGenerationCount() {
        return generationCount;
    }
}
