package hr.fer.zemris.optjava.libopti;

import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 */
public final class DoubleArraySolutionFactory implements ISolutionFactory<DoubleArraySolution> {
    private final DoubleArraySolution mDoubleArraySolution;
    private final Random mRandom;
    private final double[] mLower;
    private final double[] mUpper;

    public DoubleArraySolutionFactory(int dimSize, Random random, double[] lower, double[] upper) {
        mRandom = random;
        mLower = lower;
        mUpper = upper;
        mDoubleArraySolution = new DoubleArraySolution(dimSize);
    }

    @Override
    public DoubleArraySolution newRandomized() {
        DoubleArraySolution doubleArraySolution = mDoubleArraySolution.duplicate();
        doubleArraySolution.randomize(mRandom, mLower, mUpper);
        return doubleArraySolution;
    }

    @Override
    public DoubleArraySolution newInstance() {
        return mDoubleArraySolution.duplicate();
    }

    @Override
    public DoubleArraySolution duplicate(DoubleArraySolution instance) {
        return instance.duplicate();
    }

    @Override
    public DoubleArraySolution[] newArray(int length) {
        DoubleArraySolution[] doubleArraySolutions = new DoubleArraySolution[length];
        for (int i = 0; i < doubleArraySolutions.length; i++) {
            doubleArraySolutions[i] = newInstance();
        }
        return doubleArraySolutions;
    }

    @Override
    public DoubleArraySolution[] newRandomizedArray(int length) {
        DoubleArraySolution[] doubleArraySolutions = newArray(length);
        for (DoubleArraySolution solution : doubleArraySolutions) {
            solution.randomize(mRandom, mLower, mUpper);
        }
        return doubleArraySolutions;
    }

    @Override
    public DoubleArraySolution[] duplicate(DoubleArraySolution[] selectionCache) {
        DoubleArraySolution[] doubleArraySolutions = newArray(selectionCache.length);
        for (int i = 0; i < selectionCache.length; i++) {
            doubleArraySolutions[i] = selectionCache[i].duplicate();
        }
        return doubleArraySolutions;
    }
}
