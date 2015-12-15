package gui;

import model.Clazz;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by ivan on 6/16/15.
 */
public class GUI extends JFrame implements Canvas.IListener {
    private final Model mModel;
    private final JTextArea mTextArea;


    private final Canvas mCanvas;
    private final JPanel mToolsPanel;
    private final JCheckBox mTweakEtaCheckbox;
    private final JLabel mEtaLabel;
    private final JSlider mEpsilonSlider;
    private final JLabel mEpsilonLabel;
    private final JSlider mMaxIterSlider;
    private final JLabel mMaxIterLabel;
    private Clazz mCanvasClazz = Clazz.values()[0];
    private final JSlider mEtaSlider;
    private final JCheckBox mTestCheckbox;

    public GUI(Model model) throws IOException {
        mModel = model;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new BorderLayout());

        mToolsPanel = new JPanel();
        panel.add(mToolsPanel, BorderLayout.EAST);
        mToolsPanel.setLayout(new BoxLayout(mToolsPanel, BoxLayout.PAGE_AXIS));


        mTweakEtaCheckbox = new JCheckBox("Tweak eta");
        mEtaSlider = new JSlider();
        mEtaSlider.setMinimum(-20);
        mEtaSlider.setMaximum(0);
        mEtaSlider.addChangeListener(x -> updateGUI());
        mTweakEtaCheckbox.addChangeListener(x -> updateGUI());
        mEtaLabel = new JLabel();


        mEpsilonSlider = new JSlider();
        mEpsilonSlider.setMinimum(-10);
        mEpsilonSlider.setMaximum(0);
        mEpsilonSlider.addChangeListener(x -> updateGUI());
        mEpsilonLabel = new JLabel();


        mMaxIterSlider = new JSlider();
        mMaxIterSlider.setMinimum(0);
        mMaxIterSlider.setMaximum(9 * 2);
        mMaxIterSlider.addChangeListener(x -> updateGUI());
        mMaxIterLabel = new JLabel();

        JButton trainButton = new JButton("Train");
        trainButton.addActionListener(e -> mModel.onTrainSelected(calculateEta(), calculateEpsilon(), (int) calculateMaxIter()));
        mToolsPanel.add(trainButton);

        mTestCheckbox = new JCheckBox("Testing");
        mTestCheckbox.setSelected(false);
        mToolsPanel.add(mTestCheckbox);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undo());
        mToolsPanel.add(undoButton);


        mToolsPanel.add(new JLabel("ETA:"));
        mToolsPanel.add(mEtaSlider);
        mToolsPanel.add(mTweakEtaCheckbox);
        mToolsPanel.add(mEtaLabel);


        mToolsPanel.add(new JLabel("Error:"));
        mToolsPanel.add(mEpsilonSlider);
        mToolsPanel.add(mEpsilonLabel);


        mToolsPanel.add(new JLabel("Max iter:"));
        mToolsPanel.add(mMaxIterSlider);
        mToolsPanel.add(mMaxIterLabel);


        ButtonGroup buttonGroup = new ButtonGroup();
        for (Clazz c : Clazz.values()) {
            JRadioButton radioButton = new JRadioButton(c.name());
            radioButton.addActionListener(e -> updateState(c));
            mToolsPanel.add(radioButton);
            buttonGroup.add(radioButton);
        }
        buttonGroup.getElements().nextElement().setSelected(true);

        mTextArea = new JTextArea();
        mToolsPanel.add(mTextArea);

        mCanvas = Canvas.create();
        panel.add(mCanvas, BorderLayout.CENTER);
        mCanvas.setListener(this);

        mModel.preloadFromDisk();

        mMaxIterSlider.setValue(6);
        mEpsilonSlider.setValue(-6);
        mEtaSlider.setValue(0);
        mTweakEtaCheckbox.setSelected(true);

        updateGUI();
    }

    private void undo() {
        mModel.onUndo();

        updateGUI();
    }

    private void updateGUI() {
         mTextArea.setText(mModel.getDatasetInfo());
        DecimalFormat formatter = new DecimalFormat("0E0");
        mEtaLabel.setText(formatter.format(calculateEta()));
        mEpsilonLabel.setText(formatter.format(calculateEpsilon()));
        mMaxIterLabel.setText(formatter.format(calculateMaxIter()));
    }

    private double calculateMaxIter() {
        return Math.pow(10, mMaxIterSlider.getValue() / 2.0);
    }

    private double calculateEpsilon() {
        return Math.pow(10, mEpsilonSlider.getValue() / 2.0);
    }

    private double calculateEta() {
        return Math.pow(10, mEtaSlider.getValue() / 2.0) / (mTweakEtaCheckbox.isSelected() ? mModel.getSize() : 1);
    }

    private void updateState(Clazz c) {
        mCanvasClazz = c;
    }

    @Override
    public void onPointsCollected(int[] xes, int[] yes) {
        assert xes.length == yes.length;
        mModel.onPointsCollected(mCanvasClazz, xes, yes, mTestCheckbox.isSelected());
        updateGUI();
    }


    public static void main(String[] arg) throws IOException {
        Model model = new Model();
        GUI gui = new GUI(model);
        gui.setBounds(100, 100, 100 + 640, 100 + 480);
        gui.setVisible(true);
    }
}
