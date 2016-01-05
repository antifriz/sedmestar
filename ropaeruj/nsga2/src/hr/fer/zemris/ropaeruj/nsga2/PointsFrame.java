package hr.fer.zemris.ropaeruj.nsga2;

import javax.swing.*;
import java.util.List;

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

    public void newData(List<List<Chromosome>> fronts) {
        mSurface.drawChromosomes(fronts);
    }
}
