package hr.fer.zemris.nenr.zad7;

import java.util.Arrays;

/**
 * Created by ivan on 11/16/15.
 */
public final class FFANN {

    private final int mWeightsCount;
    private final INeuron[] mNeurons;
    private final IReadOnlyDataset mDataset;

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

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        int neuronsCount = mNeurons.length;
        double[] neuronOutputs = new double[mNeurons.length];

        for (int i1 = 0; i1 < inputs.length; i1++) {
            mNeurons[i1].setOutput(inputs[i1]);
        }

        for (int i1 = 0; i1 < neuronsCount; i1++) {
            neuronOutputs[i1] = mNeurons[i1].getOutput(neuronOutputs, weights);
        }

        int srcPos = neuronsCount - outputs.length - 1;
        System.arraycopy(neuronOutputs, srcPos, outputs, 0, outputs.length);
    }

    public int getWeightsCount() {
        return mWeightsCount;
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


    public double evaluate(final double[] weights) {
        final int outputDimension = mDataset.getOutputDimension();
        final int inputDimension = mDataset.getInputDimension();
        double sum = 0;
        final double[] neuronOutputs = new double[mNeurons.length];
        final int neuronsCount = mNeurons.length;
        int offset = neuronsCount - outputDimension - 1;
        for (double[][] sample : mDataset) {

            double[] sampleInput = sample[0];
            for (int i1 = 0; i1 < inputDimension; i1++) {
                mNeurons[i1].setOutput(sampleInput[i1]);
            }

            for (int i1 = 0; i1 < neuronsCount; i1++) {
                neuronOutputs[i1] = mNeurons[i1].getOutput(neuronOutputs, weights);
            }

            double[] sampleOutput = sample[1];
            for (int i = 0; i < outputDimension; i++) {
                double diff = neuronOutputs[(i + offset)] - sampleOutput[i];
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
