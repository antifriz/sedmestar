package hr.fer.zemris.ropaeruj.dz7;

/**
 * Created by ivan on 11/16/15.
 */
public interface IReadOnlyDataset {
    int getSize();

    int getInputDimension();

    int getOutputDimension();

    default double[] getInputAt(int idx) {
        return getSampleAt(idx)[0];
    }

    default double[] getOutputAt(int idx) {
        return getSampleAt(idx)[1];
    }

    double[][] getSampleAt(int idx);
}
