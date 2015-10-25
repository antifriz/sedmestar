package hr.fer.zemris.optjava.libopti;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public final class DoubleArrayUnifNeighborhood implements INeighborhood<DoubleArraySolution> {
    private final double[] mDeltas;
    Random mRandom;
    private double mFactor = 1;

    public DoubleArrayUnifNeighborhood(double[] deltas, Random random) {
        mDeltas = Arrays.copyOf(deltas, deltas.length);
        mRandom = random;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        DoubleArraySolution neighbor = solution.duplicate();
        for (int i = 0; i < solution.values.length; i++) {
            neighbor.values[i] += mDeltas[i] * 2 * mRandom.nextFloat() * mFactor - 1;
        }
        return neighbor;
    }

    @Override
    public void setFactor(double factor) {
        mFactor = factor;
    }
}
