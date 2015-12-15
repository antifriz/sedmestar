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
            mWeights[i] = i + 1;
        }
        mWeights = ThreadLocalRandom.current().doubles().limit(mWeights.length).map(x -> 2 * x - 1).toArray();
        mDataset.reset();
        int[] layers = mFfann.getLayers();

        mEta = 1.0 / mDataset.getWhole().size() * 2;

        double etaRatio = Math.exp((Math.log(0.1) - Math.log(mEta)) / maxIterCount);
        long start = System.currentTimeMillis();
        System.out.println(start);
        int iterCount = 1;
        while (true) {
            double error = calculateError(mDataset.getWhole(), mWeights);

            if (iterCount % 100 == 0) {
                System.out.printf("[%5d] %6.4f %6.4f %8.4f\n", iterCount, error, mEta, Math.sqrt(Arrays.stream(mWeights).map(x -> x * x).sum())/*, Arrays.toString(mWeights)*/);
                System.out.println(System.currentTimeMillis() - start);
            }

            if (error <= epsilon || iterCount > maxIterCount) {
                break;
            }

            initCache();

            updateWeights(layers);

            mDataset.next();
            iterCount++;
            mEta *= etaRatio;
        }

        return mWeights;
    }

    private void updateWeights(int[] layers) {
        final int size = mDataset.getSize();
        final FFANN.INeuron[] neurons = mFfann.mNeurons;
        final int length = layers.length;
        {
            final int k = length - 2;
            final int kLayerStartIdx = mFfann.getLayerStartIdx(k);
            final int kPlus1LayerStartIdx = mFfann.getLayerStartIdx(k + 1);
            final int jMax = layers[k + 1];
            for (int j = 0; j < jMax; j++) {
                final int jWeightStartIdx = neurons[kPlus1LayerStartIdx + j].getWeightFrom();
                final int iMax = layers[k] + 1;
                for (int i = 0; i < iMax; i++) {
                    final int ithNeuronIdx = kLayerStartIdx + i;
                    double val = 0;
                    for (int s = 0; s < size; s++) {
                        final double y1 = mNeuronOutputs.get(s)[kPlus1LayerStartIdx + j];
                        final double delta = y1 * (1 - y1) * -mOutputDiff.get(s)[j];
                        mDeltas[s][kPlus1LayerStartIdx + j] = delta;
                        val += delta * mNeuronOutputs.get(s)[ithNeuronIdx];
                    }
                    mWeights[jWeightStartIdx + i] += val * mEta;
                }
            }
        }
        for (int k = length - 3; k >= 0; k--) {
            final int kLayerStartIdx = mFfann.getLayerStartIdx(k);
            final int kPlus1LayerStartIdx = mFfann.getLayerStartIdx(k + 1);
            final int kPlus2LayerStartIdx = mFfann.getLayerStartIdx(k + 2);
            final int kPlus1LayerLength = mFfann.getLayers()[k + 2];
            final int jMax = layers[k + 1];
            for (int j = 0; j < jMax; j++) {
                final int jWeightStartIdx = neurons[kPlus1LayerStartIdx + j].getWeightFrom();
                final int iMax = layers[k] + 1;
                for (int i = 0; i < iMax; i++) {
                    final int ithNeuronIdx = kLayerStartIdx + i;
                    double val = 0;
                    for (int s = 0; s < size; s++) {
                        double sum = 0;
                        final double[] deltaS = mDeltas[s];
                        for (int o = 0; o < kPlus1LayerLength; o++) {
                            sum += deltaS[kPlus2LayerStartIdx + o] * mWeights[neurons[kPlus2LayerStartIdx + o].getWeightFrom() + j];
                        }
                        final double y1 = mNeuronOutputs.get(s)[kPlus1LayerStartIdx + j];
                        final double delta = y1 * (1 - y1) * sum;
                        deltaS[kPlus1LayerStartIdx + j] = delta;
                        final double y = mNeuronOutputs.get(s)[ithNeuronIdx];
                        val += delta * y;
                    }
                    mWeights[jWeightStartIdx + i] += val * mEta;
                }
            }
        }
    }

    private void initCache() {
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

        mDeltas = new double[mNeuronOutputs.size()][mNeuronOutputs.get(0).length];
    }

    private double getInnerDelta(int s, int j, int k) {
        double y = getY(s, j, k);
        double sum = 0;
        for (int o = 0; o < mFfann.getLayers()[k + 1]; o++) {
            sum += getPrecalculatedDelta(s, o, k + 1) * getW(j, o, k);
        }
        double delta = y * (1 - y) * sum;
        setPrecalculatedDelta(s, j, k, delta);
        return delta;
    }

    private double getPrecalculatedDelta(int s, int j, int k) {
        return mDeltas[s][mFfann.getLayerStartIdx(k) + j];
    }

    private void setPrecalculatedDelta(int s, int j, int k, double val) {
        mDeltas[s][mFfann.getLayerStartIdx(k) + j] = val;
    }

    private double getW(int i, int j, int k) {
        return mWeights[getWIdx(i, j, k)];
    }

    private double getOuterDelta(int s, int j, int k) {
        double outDiff = -mOutputDiff.get(s)[j];
        double y = getY(s, j, k);
        double delta = y * (1 - y) * outDiff;
        setPrecalculatedDelta(s, j, k, delta);
        return delta;
    }

    private double getY(int s, int i, int k) {
        return mNeuronOutputs.get(s)[mFfann.getLayerStartIdx(k) + i];
    }

    private int getWIdx(int i, int j, int k) {
        return mFfann.mNeurons[mFfann.getLayerStartIdx(k + 1) + j].getWeightFrom() + i;
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
        return sum / count / mFfann.getOutputDimension();// / 2;
    }
}
