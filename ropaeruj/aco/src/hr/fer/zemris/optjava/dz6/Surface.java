package hr.fer.zemris.optjava.dz6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by ivan on 11/15/15.
 */

class Surface extends JPanel implements ActionListener {

    private final int DELAY = 150;
    private Timer timer;
    private Ant mBestSoFar;
    private Double[][] mEuclidians;

    public Surface() {

    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;



        int w = getWidth();
        int h = getHeight();

        double minX= mEuclidians[0][0];
        double maxX= mEuclidians[0][0];
        double minY= mEuclidians[0][1];
        double maxY= mEuclidians[0][1];

        for (Double[] mEuclidian : mEuclidians) {
            minX = Math.min(minX, mEuclidian[0]);
            maxX = Math.max(maxX, mEuclidian[0]);
            minY = Math.min(minY, mEuclidian[1]);
            maxY = Math.max(maxY, mEuclidian[1]);
        }

        Iterator<Integer> it = mBestSoFar.iterator();

        int first = it.next();
        int next = first;

        while(it.hasNext()){
            int last = next;

            next = it.next();

            Double x1 = mEuclidians[last][0];
            Double y1 = mEuclidians[last][1];
            Double x2 = mEuclidians[next][0];
            Double y2 = mEuclidians[next][1];


            int xx1 = (int) ((x1-minX)/(maxX-minX) * w);
            int yy1 = h-(int) ((y1-minY)/(maxY-minY) * h);
            int xx2 = (int) ((x2-minX)/(maxX-minX) * w);
            int yy2 = h-(int) ((y2-minY)/(maxY-minY) * h);

            g2d.setPaint(Color.red);
            g2d.setStroke(new BasicStroke(2));

            g2d.drawLine(xx1, yy1, xx2, yy2);

            g2d.setPaint(Color.black);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(xx1, yy1, xx1, yy1);

        }


        Double x1 = mEuclidians[next][0];
        Double y1 = mEuclidians[next][1];
        Double x2 = mEuclidians[first][0];
        Double y2 = mEuclidians[first][1];
        int xx1 = (int) ((x1-minX)/(maxX-minX) * w);
        int yy1 = h-(int) ((y1-minY)/(maxY-minY) * h);
        int xx2 = (int) ((x2-minX)/(maxX-minX) * w);
        int yy2 = h-(int) ((y2-minY)/(maxY-minY) * h);


        g2d.setPaint(Color.red);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(xx1, yy1, xx2, yy2);


        g2d.setPaint(Color.black);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(xx2, yy2, xx2, yy2);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if(mBestSoFar!=null){
            doDrawing(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void drawAnt(Ant bestSoFar, Double[][] euclidians) {
        mBestSoFar = bestSoFar;
        mEuclidians = euclidians;

        revalidate();
        invalidate();
        repaint();
    }
}
