package hr.fer.zemris.optjava.libopti;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public class DoubleArrayUnifNeighborhood implements INeighborhood<DoubleArraySolution> {
    private final double[] mDeltas;
    Random mRand;

    DoubleArrayUnifNeighborhood(double[] deltas) {
        mDeltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution o) {
        throw new NotImplementedException();
    }
}
