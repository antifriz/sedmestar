package hr.fer.zemris.optjava.libopti;

import java.text.DecimalFormat;
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

    public DoubleArraySolution duplicate() {
        DoubleArraySolution doubleArraySolution = newLikeThis();
        System.arraycopy(values, 0, doubleArraySolution.values, 0, values.length);
        return doubleArraySolution;
    }

    public void randomize(Random random, double[] lowerLimits, double[] upperLimits) {
        for (int i = 0; i < values.length; i++) {
            values[i] = lowerLimits[i] + (upperLimits[i] - lowerLimits[i]) * random.nextFloat();
        }
    }

    @Override
    public String toString() {
        double[] a = values;
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(df.format(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
