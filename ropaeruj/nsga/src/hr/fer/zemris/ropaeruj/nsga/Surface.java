package hr.fer.zemris.ropaeruj.nsga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by ivan on 11/15/15.
 */

class Surface extends JPanel implements ActionListener {

    private final int DELAY = 150;
    private Timer timer;
    private List<List<Chromosome>> mFronts;

    public Surface() {

    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;


        int w = getWidth();
        int h = getHeight();

        float cl = 0;
        float clstep =0.75f/mFronts.size();

        g2d.setStroke(new BasicStroke(4));
        for(List<Chromosome> front:mFronts){
            g2d.setColor(Color.getHSBColor(cl,1,1));
            cl+=clstep;
            for(Chromosome c:front) {
                int x1 = (int) ((c.evaluation[0]/0.9-0.1) * w);
                int y1 = h-(int) (c.evaluation[1] / 10* h);
                g2d.drawLine(x1, y1, x1,y1);

            }

        }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if(mFronts!=null){
            doDrawing(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void drawChromosomes(List<List<Chromosome>> fronts) {
        mFronts = fronts;

        revalidate();
        invalidate();
        repaint();
    }
}
