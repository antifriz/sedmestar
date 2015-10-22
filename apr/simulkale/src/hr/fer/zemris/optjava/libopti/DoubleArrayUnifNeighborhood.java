package hr.fer.zemris.optjava.libopti;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public final class DoubleArrayUnifNeighborhood implements INeighborhood<DoubleArraySolution> {
    private final double[] mDeltas;
    Random mRand;

    public DoubleArrayUnifNeighborhood(double[] deltas) {
        mDeltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        DoubleArraySolution neighbor = solution.duplicate();
        for (int i = 0; i < solution.values.length; i++) {
            neighbor.values[i] += mDeltas[i] * 2 * mRand.nextFloat() - 1;
        }
        return neighbor;
    }
}
