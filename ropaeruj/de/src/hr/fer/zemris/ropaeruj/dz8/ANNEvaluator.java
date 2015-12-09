package hr.fer.zemris.ropaeruj.dz8;

import hr.fer.zemris.ropaeruj.dz7.IReadOnlyDataset;

/**
 * Created by ivan on 12/9/15.
 */
public abstract class ANNEvaluator {
    abstract double evaluate(double[] weights);

    abstract int getSolutionDimension();

    public static ANNEvaluator createFor(ANN ann, IReadOnlyDataset dataset) {
        if (ann instanceof FFANN) {
            return new FANNEvaluator((FFANN) ann, dataset);
        }
        return null;//return new ANNEvaluator(ann, dataset);
    }

    private static class FANNEvaluator extends ANNEvaluator {

        private final FFANN mFfann;
        private final IReadOnlyDataset mDataset;

        public FANNEvaluator(FFANN ffann, IReadOnlyDataset dataset) {
            mFfann = ffann;
            mDataset = dataset;
        }

        @Override
        double evaluate(double[] weights) {
            int outputDimension = mDataset.getOutputDimension();
            double[] output = new double[outputDimension];
            double sum = 0;
            for (double[][] data : mDataset) {
                mFfann.calcOutputs(data[0], weights, output);

                double[] realOutput = data[1];
                for (int i = 0; i < outputDimension; i++) {
                    double v = output[i] - realOutput[i];
                    sum += v * v;
                }
            }
            return sum / mDataset.getSize();
        }

        @Override
        int getSolutionDimension() {
            return 0;
        }
    }
}
