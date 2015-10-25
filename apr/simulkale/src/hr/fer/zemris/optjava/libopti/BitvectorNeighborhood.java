package hr.fer.zemris.optjava.libopti;

import java.util.Random;

/**
 * Created by ivan on 10/25/15.
 */
public class BitvectorNeighborhood implements INeighborhood<BitvectorSolution> {
    private final int mBitCount;
    private final Random mRandom;
    private double mFactor;

    public BitvectorNeighborhood(Random random, int bitCount) {
        mRandom = random;
        mBitCount = bitCount;
    }

    @Override
    public BitvectorSolution randomNeighbor(BitvectorSolution solution) {
        BitvectorSolution neighbor = solution.duplicate();
        int idxToMutate = (int) (mRandom.nextDouble() * mBitCount);
        neighbor.mBytes[idxToMutate >> 3] ^= 0b10000000 >>> (idxToMutate % 8);
        return neighbor;
    }

    @Override
    public void setFactor(double factor) {
        mFactor = factor;
    }
}
