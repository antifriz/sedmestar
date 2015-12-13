package ann;

/**
 * Created by ivan on 12/9/15.
 */
public class ANNEvaluator {
    public static ANNEvaluator createFor(ANN ann, IReadOnlyDataset dataset) {
        return new ANNEvaluator(ann, dataset);
    }


    private final ANN mAnn;
    private final IReadOnlyDataset mDataset;

    private ANNEvaluator(ANN ann, IReadOnlyDataset dataset) {
        mAnn = ann;
        mDataset = dataset;
    }

    double evaluate(double[] weights) {
        mAnn.reset();
        int outputDimension = mDataset.getOutputDimension();
        double[] output = new double[outputDimension];
        double sum = 0;
        for (double[][] data : mDataset) {
            mAnn.calcOutputs(data[0], weights, output);
            double[] realOutput = data[1];
            for (int i = 0; i < outputDimension; i++) {
                double v = output[i] - realOutput[i];
                sum += v * v;
            }
        }
        return sum / mDataset.getSize();
    }

    int getSolutionDimension() {
        return mAnn.getWeightsCount();
    }
}
