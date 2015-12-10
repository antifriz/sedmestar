package hr.fer.zemris.ropaeruj.dz8;

import java.util.Arrays;

/**
 * Created by ivan on 12/10/15.
 */
public class ElmanANN extends FFANN {
    private final int mMemoryNeurons;
    private boolean mIsInInitialState = true;

    private ElmanANN(int[] layers, ITransferFunction[] transferFunctions, int memoryNeurons) {
        super(layers, transferFunctions);
        mMemoryNeurons = memoryNeurons;
    }

    public static ElmanANN create(int[] layers, ITransferFunction transferFunction) {
        ITransferFunction[] transferFunctions = new ITransferFunction[layers.length - 1];
        Arrays.fill(transferFunctions, transferFunction);

        layers[0] += layers[1];
        return new ElmanANN(layers, transferFunctions, layers[1]);
    }

    @Override
    public int getInputDimension() {
        return super.getInputDimension() - mMemoryNeurons;
    }

    @Override
    public int getWeightsCount() {
        return super.getWeightsCount() + mMemoryNeurons;
    }

    @Override
    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        int inputsLength = inputs.length;
        double[] elmanInputs = new double[inputsLength + mMemoryNeurons];
        double[] elmanWeights = new double[weights.length - mMemoryNeurons];
        System.arraycopy(weights, mMemoryNeurons, elmanWeights, 0, weights.length - mMemoryNeurons);
        if (mIsInInitialState) {
            mIsInInitialState = false;
            System.arraycopy(weights, 0, elmanInputs, inputsLength, mMemoryNeurons);
        } else {
            System.arraycopy(mNeuronOutputs, elmanInputs.length + 1, elmanInputs, inputsLength, mMemoryNeurons - inputsLength);
        }
        System.arraycopy(inputs,0,elmanInputs,0,inputsLength);
        super.calcOutputs(elmanInputs, elmanWeights, outputs);
    }

    @Override
    public void reset() {
        super.reset();
        mIsInInitialState = true;
    }
}
