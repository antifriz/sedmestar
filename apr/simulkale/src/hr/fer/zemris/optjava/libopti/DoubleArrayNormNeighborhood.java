package hr.fer.zemris.optjava.libopti;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public class DoubleArrayNormNeighborhood implements INeighborhood<DoubleArraySolution> {
    private final double[] mDeltas;
    Random mRand;

    public DoubleArrayNormNeighborhood(double[] deltas) {
        mDeltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution o) {
        throw new NotImplementedException();
    }
}
