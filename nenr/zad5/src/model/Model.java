package model;

import ann.FFANN;
import ann.SigmoidTransferFunction;
import gui.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ivan on 12/12/15.
 */
public class Model implements GUI.IListener {

    private List<CharacterObject> mCharacters;

    public Model() {
        mCharacters = new ArrayList<>();
    }


    @Override
    public void onPointsCollected(Clazz canvasClazz, int[] xes, int[] yes) {
        CharacterObject character = new CharacterObject(canvasClazz, xes, yes);
        mCharacters.add(character);
    }

    @Override
    public void onUndo() {
        mCharacters.remove(mCharacters.size() - 1);
    }

    @Override
    public void onTrainSelected() {
        int featureSize = 20;
        FFANN ffann = FFANN.create(new int[]{featureSize, 6, Clazz.values().length}, new SigmoidTransferFunction());
        CharacterObject lastCharacter = mCharacters.get(mCharacters.size() - 1);


        List<double[][]> rawDataset = mCharacters.stream().map(x -> {
            double[] output = new double[Clazz.values().length];
            output[x.getClazzId()] = 1;
            return new double[][]{x.getFeatures(featureSize), output};
        }).collect(Collectors.toList());

        BatchReadOnlyDataset batchReadOnlyDataset = new BatchReadOnlyDataset(rawDataset, 1);


        double[] weights = new double[ffann.getWeightsCount()];


        double[] outputs = new double[Clazz.values().length];
        double[] features = lastCharacter.getFeatures(featureSize);
        System.out.println(Arrays.toString(features));
        ffann.calcOutputs(features, weights, outputs);
        System.out.println(Arrays.toString(outputs));
    }

    @Override
    public void onTestSelected() {

    }
}
