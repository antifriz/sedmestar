package ann;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivan on 12/13/15.
 */
public class BackpropAlg {

    private FFANN mFfann;
    private IReadOnlyDataset mDataset;
    private List<double[]> mOutputDiff;
    private List<double[]> mNeuronOutputs;
    private double[] mWeights;
    private double mEta = 0.01;

    public BackpropAlg(FFANN ffann, IReadOnlyDataset dataset) {
        mFfann = ffann;
        mDataset = dataset;
    }

    public double[] run(double epsilon, int maxIterCount) {
        mWeights = new double[mFfann.getWeightsCount()];
        mDataset.reset();
        int[] layers = mFfann.getLayers();

        int iterCount = 0;
        while (true) {
            double error = calculateError(mDataset.getWhole(), mWeights);
            System.out.printf("[%5d] %f\n", iterCount, error);

            if (error <= epsilon || iterCount> maxIterCount) {
                break;
            }

            mOutputDiff = new ArrayList<>();
            mNeuronOutputs = new ArrayList<>();


            for (double[][] data : mDataset) {
                double[] outputs = new double[mFfann.getOutputDimension()];
                mFfann.calcOutputs(data[0], mWeights, outputs);

                for (int i = 0; i < outputs.length; i++) {
                    outputs[i] -= data[1][i];
                }

                mOutputDiff.add(outputs);
                mNeuronOutputs.add(Arrays.copyOf(mFfann.mNeuronOutputs, mFfann.mNeuronOutputs.length));
            }

            for (int k = layers.length - 2; k >= 0; k--) {
                for (int i = 0; i < layers[k]; i++) {
                    for (int j = 0; j < layers[k + 1]; j++) {
                        double val = 0;
                        for (int s = 0; s < mDataset.getSize(); s++) {
                            val += getDelta(s, j, k);
                        }
                        val *= mEta;
                        incW(i, j, k, val);
                    }
                }
            }

            mDataset.next();
            iterCount++;
        }

        return mWeights;
    }

    private double getDelta(int s, int j, int k) {
        if (k == mFfann.getLayers().length - 2) {
            return getOuterDelta(s, j, k);
        } else {
            return getInnerDelta(s, j, k);
        }
    }

    private double getInnerDelta(int s, int j, int k) {
        double y = getY(s, j, k);
        double sum = 0;
        for (int o = 0; o < mFfann.getLayers()[k + 2]; o++) {
            sum += getDelta(s, o, k + 1) * getW(j, o, k + 1);
        }
        return y * (1 - y) * sum;
    }

    private double getW(int i, int j, int k) {
        return mWeights[getWIdx(i, j, k)];
    }

    private double getOuterDelta(int s, int j, int k) {
        double outDiff = -mOutputDiff.get(s)[j];
        double y = mNeuronOutputs.get(s)[mFfann.getLayerStartIdx(k) + j];
        return y * (1 - y) * outDiff;
    }

    private double getY(int s, int i, int k) {
        return mNeuronOutputs.get(s)[mFfann.getLayerStartIdx(k) + i];
    }

    private int getWIdx(int i, int j, int k) {
        return mFfann.mNeurons[mFfann.getLayerStartIdx(k+1) + j].getWeightFrom() + i;
    }

    private double incW(int i, int j, int k, double val) {
        return mWeights[getWIdx(i, j, k)] += val;
    }

    private double calculateError(List<double[][]> whole, double[] weights) {
        int outputDimension = mDataset.getOutputDimension();
        double[] output = new double[outputDimension];
        double sum = 0;
        double count = 0;
        for (double[][] data : whole) {
            count++;
            mFfann.calcOutputs(data[0], weights, output);
            double[] realOutput = data[1];
            for (int i = 0; i < outputDimension; i++) {
                double v = output[i] - realOutput[i];
                sum += v * v;
            }
        }
        return sum / count;// / 2;
    }
}
