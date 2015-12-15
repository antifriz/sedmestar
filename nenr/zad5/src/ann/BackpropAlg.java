package ann;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Created by ivan on 12/13/15.
 */
public class BackpropAlg {

    private FFANN mFfann;
    private IReadOnlyDataset mDataset;
    private double[] mWeights;
    private double mEta = 1;

    public BackpropAlg(FFANN ffann, IReadOnlyDataset dataset) {
        mFfann = ffann;
        mDataset = dataset;
    }

    public double[] run(double epsilon, int maxIterCount) {
        prepare();

        int[] layers = mFfann.getLayers();

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


            updateWeights(layers);

            mDataset.next();
            iterCount++;
            mEta *= etaRatio;
        }

        return mWeights;
    }

    private void prepare() {
        mWeights = new double[mFfann.getWeightsCount()];
        for (int i = 0; i < mWeights.length; i++) {
            mWeights[i] = i + 1;
        }
        mWeights = ThreadLocalRandom.current().doubles().limit(mWeights.length).map(x -> 2 * x - 1).toArray();
        mDataset.reset();

        mEta = 1.0 / mDataset.getWhole().size() * 2;
    }

    private void updateWeights(int[] layers) {
        List<double[]> outputDiff = new ArrayList<>();
        List<double[]> neuronOutputs = new ArrayList<>();


        for (double[][] data : mDataset) {
            double[] outputs = new double[mFfann.getOutputDimension()];
            mFfann.calcOutputs(data[0], mWeights, outputs);

            for (int i1 = 0; i1 < outputs.length; i1++) {
                outputs[i1] -= data[1][i1];
            }

            outputDiff.add(outputs);
            neuronOutputs.add(Arrays.copyOf(mFfann.mNeuronOutputs, mFfann.mNeuronOutputs.length));
        }

        double[][] deltas = initializeDeltas(neuronOutputs);

        final int size = mDataset.getSize();
        final FFANN.INeuron[] neurons = mFfann.mNeurons;
        final int length = layers.length;
        {
            final int k = length - 2;
            final int kLayerStartIdx = mFfann.getLayerStartIdx(k);
            final int kPlus1LayerStartIdx = mFfann.getLayerStartIdx(k + 1);
            final int jMax = layers[k + 1];
            IntStream.range(0, jMax).parallel().forEach(j -> {
                final int jWeightStartIdx = neurons[kPlus1LayerStartIdx + j].getWeightFrom();
                final int iMax = layers[k] + 1;
                for (int i = 0; i < iMax; i++) {
                    final int ithNeuronIdx = kLayerStartIdx + i;
                    double val = 0;
                    for (int s = 0; s < size; s++) {
                        final double y1 = neuronOutputs.get(s)[kPlus1LayerStartIdx + j];
                        final double delta = y1 * (1 - y1) * -outputDiff.get(s)[j];
                        deltas[s][kPlus1LayerStartIdx + j] = delta;
                        val += delta * neuronOutputs.get(s)[ithNeuronIdx];
                    }
                    mWeights[jWeightStartIdx + i] += val * mEta;
                }
            });
        }
        for (int kk = length - 3; kk >= 0; kk--) {
            final int k = kk;
            final int kLayerStartIdx = mFfann.getLayerStartIdx(k);
            final int kPlus1LayerStartIdx = mFfann.getLayerStartIdx(k + 1);
            final int kPlus2LayerStartIdx = mFfann.getLayerStartIdx(k + 2);
            final int kPlus1LayerLength = mFfann.getLayers()[k + 2];
            final int jMax = layers[k + 1];
            IntStream.range(0, jMax).parallel().forEach(j -> {
                final int jWeightStartIdx = neurons[kPlus1LayerStartIdx + j].getWeightFrom();
                final int iMax = layers[k] + 1;
                for (int i = 0; i < iMax; i++) {
                    final int ithNeuronIdx = kLayerStartIdx + i;
                    double val = 0;
                    for (int s = 0; s < size; s++) {
                        double sum = 0;
                        final double[] deltaS = deltas[s];
                        for (int o = 0; o < kPlus1LayerLength; o++) {
                            sum += deltaS[kPlus2LayerStartIdx + o] * mWeights[neurons[kPlus2LayerStartIdx + o].getWeightFrom() + j];
                        }
                        final double y1 = neuronOutputs.get(s)[kPlus1LayerStartIdx + j];
                        final double delta = y1 * (1 - y1) * sum;
                        deltaS[kPlus1LayerStartIdx + j] = delta;
                        val += delta * neuronOutputs.get(s)[ithNeuronIdx];
                    }
                    mWeights[jWeightStartIdx + i] += val * mEta;
                }
            });
        }
    }

    private double[][] initializeDeltas(List<double[]> neuronOutputs) {
        return new double[neuronOutputs.size()][neuronOutputs.get(0).length];
    }

    public double calculateError(List<double[][]> whole, double[] weights) {
        int outputDimension = mDataset.getOutputDimension();
        return whole.stream().parallel().mapToDouble(data->{
            double[] output = new double[outputDimension];
            mFfann.calcOutputs(data[0], weights, output);
            double[] realOutput = data[1];
            double sum = 0;
            for (int i = 0; i < outputDimension; i++) {
                final double v = output[i] - realOutput[i];
                sum += v * v;
            }
            return sum;
        }).average().getAsDouble()/mFfann.getOutputDimension();
    }
}
