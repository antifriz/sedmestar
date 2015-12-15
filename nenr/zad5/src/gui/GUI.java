package gui;

import model.Clazz;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by ivan on 6/16/15.
 */
public class GUI extends JFrame implements Canvas.IListener {
    private final Model mModel;
    private final JTextArea mTextArea;



    private final Canvas mCanvas;
    private final JPanel mToolsPanel;
    private Clazz mCanvasClazz = Clazz.values()[0];

    public GUI(Model model) throws IOException {
        mModel = model;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new BorderLayout());

        mToolsPanel = new JPanel();
        panel.add(mToolsPanel, BorderLayout.EAST);
        mToolsPanel.setLayout(new BoxLayout(mToolsPanel, BoxLayout.PAGE_AXIS));


        JButton trainButton = new JButton("Train");
        trainButton.addActionListener(e -> mModel.onTrainSelected());
        mToolsPanel.add(trainButton);

        JButton testButton = new JButton("Test");
        testButton.addActionListener(e -> mModel.onTestSelected());
        mToolsPanel.add(testButton);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undo());
        mToolsPanel.add(undoButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        for (Clazz c : Clazz.values()) {
            JRadioButton radioButton = new JRadioButton(c.name());
            radioButton.addActionListener(e -> updateState(c));
            mToolsPanel.add(radioButton);
            buttonGroup.add(radioButton);
        }
        buttonGroup.getElements().nextElement().setSelected(true);

        mTextArea = new JTextArea();
        updateGUI();
        mToolsPanel.add(mTextArea);

        mToolsPanel.setBackground(Color.blue);


        mCanvas = Canvas.create();
        panel.add(mCanvas, BorderLayout.CENTER);
        mCanvas.setBackground(Color.red);
        mCanvas.setListener(this);

        mModel.preloadFromDisk();
        updateGUI();
    }

    private void undo() {
        mModel.onUndo();

        updateGUI();
    }

    private void updateGUI() {
        mTextArea.setText(mModel.getDatasetInfo());
    }

    private void updateState(Clazz c) {
        mCanvasClazz = c;
    }

    @Override
    public void onPointsCollected(int[] xes, int[] yes) {
        assert xes.length == yes.length;
        mModel.onPointsCollected(mCanvasClazz, xes, yes);
        updateGUI();
    }


    public static void main(String[] arg) throws IOException {
        Model model = new Model();
        GUI gui = new GUI(model);
        gui.setBounds(100, 100, 500, 400);
        gui.setVisible(true);
    }
}
