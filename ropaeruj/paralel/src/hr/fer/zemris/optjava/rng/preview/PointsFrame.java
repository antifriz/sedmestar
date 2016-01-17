package hr.fer.zemris.optjava.rng.preview;

import hr.fer.zemris.optjava.rng.image.GrayScaleImage;

import javax.swing.*;

/**
 * Created by ivan on 11/15/15.
 */
public class PointsFrame extends JFrame {

    private Surface mSurface;

    public PointsFrame(int width, int height) {

        mSurface = new Surface();
        add(mSurface);


        setTitle("Points");
        setSize(width*2, height*2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void newData(GrayScaleImage image) {
        mSurface.drawImage(image);
    }
}
