package hr.fer.zemris.ropaeruj.dz8;

import java.util.stream.Stream;

/**
 * Created by ivan on 11/16/15.
 */
public interface IReadOnlyDataset extends Iterable<double[][]>{
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

    Stream<double[][]> stream();
}
