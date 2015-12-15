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
    private double[][] mDeltas;

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

        mEta = 1.0/mDataset.getWhole().size()*2;

        double etaRatio = Math.exp((Math.log(0.1)-Math.log(mEta))/maxIterCount);
        long start = System.currentTimeMillis();
        System.out.println(start);
        int iterCount = 1;
        while (true) {
            double error = calculateError(mDataset.getWhole(), mWeights);

            if(true ||iterCount%100 == 0) {
                System.out.printf("[%5d] %6.4f %6.4f %8.4f\n", iterCount, error,mEta, Math.sqrt(Arrays.stream(mWeights).map(x->x*x).sum())/*, Arrays.toString(mWeights)*/);
                System.out.println(System.currentTimeMillis()-start);
            }

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

            mDeltas = new double[mNeuronOutputs.size()][mNeuronOutputs.get(0).length];

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
            sum += getPrecalculatedDelta(s, o, k + 1) * getW(j, o, k);
        }
        double delta = y * (1 - y) * sum;
        setPrecalculatedDelta(s,j,k,delta);
        return delta;
    }

    private double getPrecalculatedDelta(int s, int j, int k){
        return mDeltas[s][mFfann.getLayerStartIdx(k) + j];
    }

    private void setPrecalculatedDelta(int s, int j, int k, double val){
        mDeltas[s][mFfann.getLayerStartIdx(k) + j] = val;
    }

    private double getW(int i, int j, int k) {
        return mWeights[getWIdx(i, j, k)];
    }

    private double getOuterDelta(int s, int j, int k) {
        double outDiff = -mOutputDiff.get(s)[j];
        double y = getY(s,j,k);
        double delta = y * (1 - y) * outDiff;
        setPrecalculatedDelta(s,j,k,delta);
        return delta;
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

    public double calculateError(List<double[][]> whole, double[] weights) {
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
        return sum / count/mFfann.getOutputDimension();// / 2;
    }
}
