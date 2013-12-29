package com.purediscovery.vennlayout.ui;

import com.purediscovery.vennlayout.model.CriteriaResult;
import com.purediscovery.vennlayout.model.SetLayout;
import com.purediscovery.vennlayout.model.SetPosition;
import com.purediscovery.vennlayout.model.GALayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 *
 */
public class RenderPanel extends JPanel {

    static final private Color selectionColor = new Color(255, 0, 0, 128);

    GALayout gaLayout;
    private AffineTransform transform;
    private CriteriaResult selection = null;


    public RenderPanel(GALayout gaLayout) {
        this.gaLayout = gaLayout;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                try {
                    selectArea(mouseEvent.getPoint());
                } catch (NoninvertibleTransformException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void selectArea(Point point) throws NoninvertibleTransformException {
        System.out.println("mouse click");

        Point2D p = viewToModel(point);

        for (CriteriaResult result : gaLayout.getBest().getCriteriaResults()) {
            if (!result.isEmpty() && result.getArea().contains(p)) {
                selection = result;
                System.out.println("selection error = " + selection.getError());
                repaint();
                return;
            }
        }

        selection = null;
        repaint();
    }


    private Point2D modelToView(Point2D point2D) {
        Point2D.Double p = new Point2D.Double();
        transform.transform(point2D, p);
        return p;
    }

    private Point2D viewToModel(Point2D point2D) throws NoninvertibleTransformException {
        Point2D.Double p = new Point2D.Double();
        transform.inverseTransform(point2D, p);
        return p;
    }


    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        RenderingHints renderHints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(renderHints);

        Dimension d = this.getSize();


        g.setColor(Color.WHITE);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.BLACK);


        //g.drawLine(0,0,d.width,d.height);
        //g.draw(AreaUtil.makeCircle(d.width/2,d.height/2, 100, 4));


        final SetLayout best = gaLayout.getBest();
        int zeroAreaCount = best.getZeroAreaCount();
        if (zeroAreaCount != 0)
            g.drawString(Integer.toString(zeroAreaCount) + " missing regions", 10, 20);


        if (transform == null)
            initTransform(d);
        g.transform(transform);

        g.setStroke(new BasicStroke(getStrokeSize(g)));

        ArrayList<SetPosition> positions = best.getSetPositions();
        for (SetPosition setPosition : positions) {
            g.setColor(setPosition.getSet().getColor());
            GeneralPath path = setPosition.getPoints();
            g.fill(setPosition.getPoints());

            g.setColor(Color.black);
            g.draw(path);

            Point2D.Float p = getLabelLocation(path);
            AffineTransform save = g.getTransform();
            g.translate(p.x, p.y);
            scaleForText(g);
            g.drawString(setPosition.getSet().getName(), 0, 0);
            g.setTransform(save);
        }

        if (selection != null && !selection.isEmpty()) {
            g.setColor(selectionColor);
            g.fill(selection.getArea());
        }


        //g.draw(ContinuousBezier.makeCircle(0,0,10).toPath());


        /*
       GeneralPath path = new GeneralPath();
       path.moveTo(10,0);
       path.curveTo(10,10, 10,10,0,10);
       path.quadTo(-10,0, -10,-10);
       path.closePath();
        */

        /*
        g.setStroke(new BasicStroke(getStrokeSize(g)));

        ContinuousBezier b = new ContinuousBezier();
        b.add(10,0, 10,20);
        b.add(-10,0, -10,-20);

        g.setColor(Color.blue);
        g.draw(b.toPath());

        g.drawRect(-10,-10,20,20);
        */
    }

    private float getStrokeSize(Graphics2D g) {
        float scale = (float) g.getTransform().getScaleX();
        return 1 / scale;
    }

    private void scaleForText(Graphics2D g) {

        double scale = g.getTransform().getScaleX();
        g.scale(1 / scale, 1 / scale);
    }

    private void initTransform(Dimension d) {

        transform = new AffineTransform();
        transform.translate(d.width / 2, d.height / 2);
        transform.scale(20, 20);

    }


    public Point2D.Float getLabelLocation(GeneralPath gp) {

        float[] p = new float[6];
        gp.getPathIterator(new AffineTransform()).currentSegment(p);
        return new Point2D.Float(p[0], p[1]);


    }
}
