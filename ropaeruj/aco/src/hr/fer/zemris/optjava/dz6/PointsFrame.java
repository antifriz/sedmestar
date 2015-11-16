package hr.fer.zemris.optjava.dz6;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by ivan on 11/15/15.
 */
public class PointsFrame extends JFrame {

    private Surface mSurface;

    public PointsFrame() {

        initUI();
    }

    private void initUI() {

        mSurface = new Surface();
        add(mSurface);


        setTitle("Points");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void newData(Ant bestSoFar, Double[][] euclidians) {
        mSurface.drawAnt(bestSoFar,euclidians);
    }
}
