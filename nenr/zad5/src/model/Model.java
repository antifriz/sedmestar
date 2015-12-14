package model;

import ann.BackpropAlg;
import ann.FFANN;
import ann.SigmoidTransferFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ivan on 12/12/15.
 */
public class Model {

    private List<CharacterObject> mCharacters;

    public Model() {
        mCharacters = new ArrayList<>();
    }


    public void onPointsCollected(Clazz canvasClazz, int[] xes, int[] yes) {
        CharacterObject character = new CharacterObject(canvasClazz, xes, yes);
        mCharacters.add(character);
    }

    public void onUndo() {
        if (mCharacters.size() > 0) {
            mCharacters.remove(mCharacters.size() - 1);
        }
    }

    public void onTrainSelected() {
        if(mCharacters.size()>0) {
            int featureSize = 2;
            FFANN ffann = FFANN.create(new int[]{featureSize, 2, Clazz.values().length}, new SigmoidTransferFunction());

            List<double[][]> rawDataset = mCharacters.stream().map(x -> {
                double[] output = new double[Clazz.values().length];
                output[x.getClazzId()] = 1;
                return new double[][]{x.getFeatures(featureSize), output};
            }).collect(Collectors.toList());

            BatchReadOnlyDataset batchReadOnlyDataset = new BatchReadOnlyDataset(rawDataset, 1);

            BackpropAlg alg = new BackpropAlg(ffann, batchReadOnlyDataset);
            double[] weights = alg.run(0.01, 1000);
            System.out.println(Arrays.toString(weights));
        }
    }

    public void onTestSelected() {

    }

    public String getDatasetInfo() {
        int[] count = new int[Clazz.values().length];
        for (CharacterObject c : mCharacters) {
            count[c.getClazzId()]++;
        }
        StringBuilder sb = new StringBuilder();
        for (Clazz c : Clazz.values()) {
            sb.append(c.name()).append("[").append(count[c.ordinal()]).append("]\n");
        }
        return sb.toString();
    }
}
