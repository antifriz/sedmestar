package gui;

import model.Clazz;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ivan on 6/16/15.
 */
public class GUI extends JFrame implements Canvas.IListener {
    private final IListener mListener;

    public interface IListener {
        void onPointsCollected(Clazz canvasClazz, int[] xes, int[] yes);

        void onUndo();

        void onTrainSelected();

        void onTestSelected();
    }

    private final Canvas mCanvas;
    private final JPanel mToolsPanel;
    private Clazz mCanvasClazz = Clazz.values()[0];

    public GUI(IListener listener) {
        mListener = listener;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new BorderLayout());

        mToolsPanel = new JPanel();
        panel.add(mToolsPanel, BorderLayout.EAST);
        mToolsPanel.setLayout(new BoxLayout(mToolsPanel, BoxLayout.PAGE_AXIS));


        JButton trainButton = new JButton("Train");
        trainButton.addActionListener(e -> mListener.onTrainSelected());
        mToolsPanel.add(trainButton);

        JButton testButton = new JButton("Test");
        testButton.addActionListener(e -> mListener.onTestSelected());
        mToolsPanel.add(testButton);


        mToolsPanel.add(new JButton("jej2"));

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> mListener.onUndo());
        mToolsPanel.add(undoButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        for (Clazz c : Clazz.values()) {
            JRadioButton radioButton = new JRadioButton(c.name());
            radioButton.addActionListener(e -> updateState(c));
            mToolsPanel.add(radioButton);
            buttonGroup.add(radioButton);
        }

        mToolsPanel.setBackground(Color.blue);


        mCanvas = Canvas.create();
        panel.add(mCanvas, BorderLayout.CENTER);
        mCanvas.setBackground(Color.red);
        mCanvas.setListener(this);
    }

    private void updateState(Clazz c) {
        mCanvasClazz = c;
    }

    @Override
    public void onPointsCollected(int[] xes, int[] yes) {
        assert xes.length == yes.length;
        mListener.onPointsCollected(mCanvasClazz, xes, yes);
    }
}
