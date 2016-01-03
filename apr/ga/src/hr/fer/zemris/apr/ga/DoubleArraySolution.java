package hr.fer.zemris.apr.ga;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public final class DoubleArraySolution extends SingleObjectiveSolution {
    public double[] values;

    public DoubleArraySolution(int n) {
        values = new double[n];
    }

    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(values.length);

    }

    public DoubleArraySolution randomize(Random random, double[] lowerLimits, double[] upperLimits) {
        for (int i = 0; i < values.length; i++) {
            values[i] = lowerLimits[i] + (upperLimits[i] - lowerLimits[i]) * random.nextFloat();
        }
        return this;
    }
}
