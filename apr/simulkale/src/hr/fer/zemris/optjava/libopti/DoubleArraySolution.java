package hr.fer.zemris.optjava.libopti;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public class DoubleArraySolution extends SingleObjectiveSolution {
    public double[] values;

    public DoubleArraySolution(int n) {
        values = new double[values.length];
    }

    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(values.length);

    }

    public DoubleArraySolution duplicate() {
        DoubleArraySolution doubleArraySolution = newLikeThis();
        System.arraycopy(values, 0, doubleArraySolution.values, 0, values.length);
        return doubleArraySolution;
    }

    public DoubleArraySolution randomize(Random random, double[] arr1, double[] arr2) {
        throw new UnsupportedOperationException();
    }
}
