package ann;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ivan on 12/13/15.
 */
public class BackpropAlg {

    private FFANN mFfann;
    private IReadOnlyDataset mDataset;
    private List<double[]> mOutputDiff;
    private List<double[]> mNeuronOutputs;
    private double[] mWeights;
    private double mEta = 1;

    public BackpropAlg(FFANN ffann, IReadOnlyDataset dataset) {
        mFfann = ffann;
        mDataset = dataset;
    }

    public double[] run(double epsilon, int maxIterCount) {
        mWeights = new double[mFfann.getWeightsCount()];
        for (int i = 0; i < mWeights.length; i++) {
            mWeights[i] = i+1;
        }
        mWeights = ThreadLocalRandom.current().doubles().limit(mWeights.length).map(x->2*x-1).toArray();
        mDataset.reset();
        int[] layers = mFfann.getLayers();

        double etaRatio = Math.exp((Math.log(0.1)-Math.log(mEta))/maxIterCount);

        int iterCount = 1;
        while (true) {
            double error = calculateError(mDataset.getWhole(), mWeights);

            //if(iterCount%1000 == 0) {
                System.out.printf("[%5d] %f %f\n", iterCount, error,mEta/*, Arrays.toString(mWeights)*/);
            //}

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
//                System.out.println(Arrays.toString(mFfann.mNeuronOutputs));
            }

            for (int k = layers.length - 2; k >= 0; k--) {
                for (int i = 0; i < layers[k] + 1; i++) {
                    for (int j = 0; j < layers[k + 1]; j++) {
                        double val = 0;
                        for (int s = 0; s < mDataset.getSize(); s++) {
                            double delta = getDelta(s, j, k+1);
                            double y = getY(s, i, k);
                            val += delta * y;
                        }
                        val *= mEta;
//                        System.out.println(val);
                        incW(i, j, k, val);
                    }
                }
            }

            mDataset.next();
            iterCount++;
            mEta*=etaRatio;
        }

        return mWeights;
    }

    private double getDelta(int s, int j, int k) {
        if (k == mFfann.getLayers().length - 1) {
            return getOuterDelta(s, j, k);
        } else {
            return getInnerDelta(s, j, k);
        }
    }

    private double getInnerDelta(int s, int j, int k) {
        double y = getY(s, j, k);
        double sum = 0;
        for (int o = 0; o < mFfann.getLayers()[k + 1]; o++) {
            sum += getDelta(s, o, k + 1) * getW(j, o, k);
        }
        return y * (1 - y) * sum;
    }

    private double getW(int i, int j, int k) {
        return mWeights[getWIdx(i, j, k)];
    }

    private double getOuterDelta(int s, int j, int k) {
        double outDiff = -mOutputDiff.get(s)[j];
        double y = getY(s,j,k);
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
