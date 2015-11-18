package hr.fer.zemris.ropaeruj.dz7;

import java.util.Arrays;

/**
 * Created by ivan on 11/16/15.
 */
public class FFANN {

    private final int mWeightsCount;
    private final INeuron[] mNeurons;
    private IReadOnlyDataset mDataset;

    public FFANN(int[] layers, ITransferFunction[] transferFunctions, IReadOnlyDataset dataset) {
        mDataset = dataset;
        assert layers.length > 1;
        assert transferFunctions.length + 1 == layers.length;

        int numberOfLayers = layers.length;


        int w = 0;
        int neuronsCount = layers[0] + 1;
        for (int i = 1; i < numberOfLayers; i++) {
            w += (layers[i - 1] + 1) * layers[i];
            neuronsCount += layers[i] + 1;
        }
        mWeightsCount = w;


        mNeurons = new INeuron[neuronsCount];
        int weightIdx = 0;
        int lastLayerFirstNeuronIdx = 0;
        int firstLayerSize = layers[0];
        int k = firstLayerSize + 1;
        for (int i = 0; i < firstLayerSize + 1; i++) {
            mNeurons[i] = new DummyNeuron();
        }

        for (int i = 1; i < numberOfLayers; i++) {
            ITransferFunction transferFunction = transferFunctions[i - 1];
            int beforeLayerSize = layers[i - 1] + 1;
            int thisLayerSize = layers[i];
            for (int j = 0; j < thisLayerSize; j++) {
                mNeurons[k++] = new RealNeuron(weightIdx, lastLayerFirstNeuronIdx, beforeLayerSize, transferFunction);
                weightIdx += beforeLayerSize;
            }
            mNeurons[k++] = new DummyNeuron();
            lastLayerFirstNeuronIdx += beforeLayerSize;
        }
    }

    public static FFANN createSigmoidal(int[] layers, IReadOnlyDataset dataset) {
        ITransferFunction[] transferFunctions = new ITransferFunction[layers.length - 1];
        Arrays.fill(transferFunctions, new SigmoidTransferFunction());

        return new FFANN(
                layers,
                transferFunctions,
                dataset);

    }

    public int getWeightsCount() {
        return mWeightsCount;
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        int neuronsCount = mNeurons.length;
        double[] neuronOutputs = new double[mNeurons.length];

        for (int i = 0; i < inputs.length; i++) {
            mNeurons[i].setOutput(inputs[i]);
        }

        for (int i = 0; i < neuronsCount; i++) {
            neuronOutputs[i] = mNeurons[i].getOutput(neuronOutputs, weights);
        }

        int srcPos = neuronsCount - outputs.length - 1;
        System.arraycopy(neuronOutputs, srcPos, outputs, 0, outputs.length);
    }

    interface INeuron {
        void setOutput(double value);

        double getOutput(double[] neuronsOutputs, double[] weights);
    }

    private static class DummyNeuron implements INeuron {

        private double mOutput = 1.0;

        @Override
        public void setOutput(double value) {
            mOutput = value;
        }

        @Override
        public double getOutput(double[] neuronsOutputs, double[] weights) {
            return mOutput;
        }
    }

    static final class RealNeuron implements INeuron {
        /*private*/ final int mWeightFrom;
        /*private*/ final int mNeuronFrom;
        private final ITransferFunction mTransferFunction;
        private final int mLength;

        RealNeuron(int weightFrom, int neuronFrom, int length, ITransferFunction transferFunction) {
            mWeightFrom = weightFrom;
            mNeuronFrom = neuronFrom;
            mLength = length;
            mTransferFunction = transferFunction;
        }


        @Override
        public void setOutput(double value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getOutput(double[] inputs, double[] weights) {
            double sum = 0;
            for (int i = 0; i < mLength; i++) {
                sum += inputs[mNeuronFrom + i] * weights[mWeightFrom + i];
            }
            return mTransferFunction.valueAt(sum);
        }
    }


    public double evaluate(double[] weights) {
        int outputDimension = mDataset.getOutputDimension();
        double[] outputs = new double[outputDimension];
        double sum = 0;
        for (double[][] sample : mDataset) {
            calcOutputs(sample[0], weights, outputs);
            for (int i = 0; i < outputDimension; i++) {
                double diff = outputs[i] - sample[1][i];
                sum += diff * diff;
            }
        }
        return sum / mDataset.getSize();
    }


    public double percentageOfGoodClassifications(double[] weights) {
        int outputDimension = mDataset.getOutputDimension();
        double[] outputs = new double[outputDimension];
        double sum = 0;
        for (double[][] sample : mDataset) {
            int subsum = 0;
            calcOutputs(sample[0], weights, outputs);
            for (int i = 0; i < outputDimension; i++) {
                if (Math.abs(outputs[i] - sample[1][i]) < 0.5) {
                    subsum++;
                }
            }
            if (subsum == outputDimension) {
                sum++;
            }
        }
        return sum / (double) mDataset.getSize();
    }


}
