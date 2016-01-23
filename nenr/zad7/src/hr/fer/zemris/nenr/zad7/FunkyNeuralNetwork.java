package hr.fer.zemris.nenr.zad7;

import java.util.Arrays;

/**
 * Created by ivan on 11/16/15.
 */
public final class FunkyNeuralNetwork {

    private int mWeightsCount = -1;
    private final int[] mLayers;
    public  final double[] mNeuronOutputs;

    public FunkyNeuralNetwork(int[] layers) {
        int mNeuronsCount = 0;
        for (int layer : layers) {
            mNeuronsCount += layer;
        }
        mLayers = Arrays.copyOf(layers,layers.length);
        mNeuronOutputs = new double[mNeuronsCount];
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        mNeuronOutputs[0] = inputs[0];
        mNeuronOutputs[1] = inputs[1];


        for (int i = 0; i < mLayers[1]; i++) {
            int refIdx = 4 * i;
            double w0 = weights[refIdx];
            double s0 = weights[refIdx + 1];
            double w1 = weights[refIdx + 2];
            double s1 = weights[refIdx + 3];

            mNeuronOutputs[2 + i] = 1.0 / (1.0 + Math.abs(mNeuronOutputs[0] - w0) / Math.abs(s0) + Math.abs(mNeuronOutputs[1] - w1) / Math.abs(s1));
        }

        int weightCntr = 4*mLayers[1];
        int outCntr = 2+mLayers[1];

        for (int i = 2; i < mLayers.length; i++) {
            int thisLayer = mLayers[i];
            int lastLayer = mLayers[i - 1];
            for (int j = 0; j < thisLayer; j++) {
                double net = 0;
                for (int k = 0; k < lastLayer; k++) {
                    net += weights[weightCntr+(lastLayer +1)*j+k] * mNeuronOutputs[outCntr- lastLayer + k];
                }
                net += weights[weightCntr + (lastLayer +1)*j+ lastLayer];
                mNeuronOutputs[outCntr + j] = 1.0/(1.0+Math.exp(-net));
            }
            weightCntr+= (lastLayer +1) * thisLayer;
            outCntr+= thisLayer;
        }
        System.arraycopy(mNeuronOutputs, mNeuronOutputs.length - outputs.length, outputs, 0, outputs.length);
    }

    public int getWeightsCount() {
        if(mWeightsCount <0){
            mWeightsCount=mLayers[1]*4;
            for (int i = 2; i < mLayers.length; i++) {
                mWeightsCount+=mLayers[i]*(mLayers[i-1]+1);
            }
        }
        return mWeightsCount;
    }


    public double evaluate(final double[] weights, IReadOnlyDataset dataset) {
        final int outputDimension = dataset.getOutputDimension();
        double sum = 0;
        final double[] outputs = new double[outputDimension];

        for (double[][] sample : dataset) {
            calcOutputs(sample[0],weights,outputs);

            for (int i = 0; i < outputDimension; i++) {
                double diff = outputs[i] - sample[1][i];
                sum += diff * diff;
            }
        }
        return sum / dataset.getSize();
    }
}
