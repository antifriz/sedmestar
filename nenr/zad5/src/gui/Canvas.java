package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 12/12/15.
 */
public class Canvas extends JComponent implements MouseListener, MouseMotionListener {
    private IListener mListener;

    public void setListener(IListener listener) {
        mListener = listener;
    }

    interface IListener {
        void onPointsCollected(int[] xes, int[] yes);
    }

    private Canvas() {
        addMouseListener(this);
        addMouseMotionListener(this);
        mPointList = new ArrayList<>();
    }

    public static Canvas create() {
        return new Canvas();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mPointList.add(e.getPoint());
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    List<Point> mPointList;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.black);

        int[] xes = mPointList.stream().mapToInt(x -> x.x).toArray();
        int[] yes = mPointList.stream().mapToInt(x -> x.y).toArray();
        g.drawPolyline(xes, yes, xes.length);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mPointList.clear();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(mPointList.size()>0) {
            int[] xes = mPointList.stream().mapToInt(x -> x.x).toArray();
            int[] yes = mPointList.stream().mapToInt(x -> x.y).toArray();
            mListener.onPointsCollected(xes, yes);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
