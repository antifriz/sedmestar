package hr.fer.zemris.optjava.libopti;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public final class DoubleArrayNormNeighborhood implements INeighborhood<DoubleArraySolution> {
    private final double[] mDeltas;
    Random mRand;
    private double mFactor = 1;

    public DoubleArrayNormNeighborhood(double[] deltas) {
        mDeltas = deltas;
        mRand = new Random();
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        DoubleArraySolution neighbor = solution.duplicate();
        for (int i = 0; i < solution.values.length; i++) {
            neighbor.values[i] += mDeltas[i] * mRand.nextGaussian() * mFactor;
        }
        return neighbor;
    }

    @Override
    public void setFactor(double factor) {
        mFactor = factor;
    }
}
