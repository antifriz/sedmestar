package hr.fer.zemris.ropaeruj.dz7;

/**
 * Created by ivan on 11/16/15.
 */
public class FFANN {

    private final int mWeightsCount;
    private final INeuron[] mNeurons;

    public FFANN(int[] layers, ITransferFunction[] transferFunctions, IReadOnlyDataset dataset) {

        assert layers.length > 1;
        assert transferFunctions.length + 1 == layers.length;

        int numberOfLayers = layers.length;


        int w = 0;
        int neuronsCount = layers[0];
        for (int i = 1; i < numberOfLayers; i++) {
            w += (layers[i - 1] + 1) * layers[i];
            neuronsCount += layers[i];
        }
        mWeightsCount = w;


        mNeurons = new INeuron[neuronsCount];
        int k = 0;
        int weightIdx = 0;
        int lastLayerFirstNeuronIdx = 0;
        int firstLayerSize = layers[0];
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
        }
    }

    public int getWeightsCount() {
        return mWeightsCount;
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        int length = weights.length;
        double[] neuronOutputs = new double[length];

        for (int i = 0; i < inputs.length; i++) {
            mNeurons[i].setOutput(inputs[i]);
        }

        for (int i = 0; i < length; i++) {
            neuronOutputs[i] = mNeurons[i].getOutput(neuronOutputs, weights);
        }

        int srcPos = length - outputs.length;
        System.arraycopy(neuronOutputs, srcPos, outputs, srcPos, length - (srcPos));
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

    private static final class RealNeuron implements INeuron {
        private final int mWeightFrom;
        private final int mNeuronFrom;
        private final ITransferFunction mTransferFunction;
        private final int mLength;
        private double mOutput;

        private RealNeuron(int weightFrom, int neuronFrom, int length, ITransferFunction transferFunction) {
            mWeightFrom = weightFrom;
            mNeuronFrom = neuronFrom;
            mLength = length;
            mTransferFunction = transferFunction;
        }


        @Override
        public void setOutput(double value) {
            mOutput = value;
        }

        @Override
        public double getOutput(double[] neuronsOutputs, double[] weights) {
            double sum = 0;
            for (int i = 0; i < mLength; i++) {
                sum += neuronsOutputs[mNeuronFrom + i] * weights[mWeightFrom + i];
            }
            return mTransferFunction.valueAt(sum);
        }
    }


}
