package ann;

import java.util.Arrays;

/**
 * Created by ivan on 11/16/15.
 */
public class FFANN {

    private final int mWeightsCount;
    public final INeuron[] mNeurons;
    private final int[] mLayers;
    protected double[] mNeuronOutputs;
    private final int[] mLayerStartIndices;

    public FFANN(int[] layers, ITransferFunction[] transferFunctions) {
        mLayers = Arrays.copyOf(layers, layers.length);
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
        mLayerStartIndices = new int[layers.length];
        mLayerStartIndices[0] = 0;
        for (int i = 1; i < numberOfLayers; i++) {
            mLayerStartIndices[i] = k;
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

        System.out.printf("Loaded "+this.getClass().getSimpleName()+" with dimension %s\n", Arrays.toString(layers));
    }

    public static FFANN create(int[] layers, ITransferFunction transferFunction) {
        ITransferFunction[] transferFunctions = new ITransferFunction[layers.length - 1];
        Arrays.fill(transferFunctions, transferFunction);
        return new FFANN(layers, transferFunctions);
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        assert weights.length == mWeightsCount;
        assert inputs.length == mLayers[0];

        int neuronsCount = mNeurons.length;
        mNeuronOutputs = new double[mNeurons.length];

        for (int i1 = 0; i1 < inputs.length; i1++) {
            mNeurons[i1].setOutput(inputs[i1]);
        }

        for (int i1 = 0; i1 < neuronsCount; i1++) {
            mNeuronOutputs[i1] = mNeurons[i1].getOutput(mNeuronOutputs, weights);
        }

        int srcPos = neuronsCount - outputs.length - 1;
        System.arraycopy(mNeuronOutputs, srcPos, outputs, 0, outputs.length);
    }

    public int getInputDimension() {
        return mLayers[0];
    }

    public void reset() {

    }

    public int getWeightsCount() {
        return mWeightsCount;
    }

    public int getOutputDimension() {
        return mLayers[mLayers.length-1];
    }

    public int[] getLayers() {
        return mLayers;
    }

    public int getLayerStartIdx(int i) {
        return mLayerStartIndices[i];
    }
    public int getLastLayerStartIdx() {
        return mLayerStartIndices[mLayerStartIndices.length-1];
    }

    interface INeuron {
        void setOutput(double value);

        double getOutput(double[] neuronsOutputs, double[] weights);

        int getWeightFrom();
        int getWeightSize();
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

        @Override
        public int getWeightFrom() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getWeightSize() {
            throw new UnsupportedOperationException();
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

        @Override
        public int getWeightFrom() {
            return mWeightFrom;
        }

        @Override
        public int getWeightSize() {
            return mLength;
        }
    }
}
