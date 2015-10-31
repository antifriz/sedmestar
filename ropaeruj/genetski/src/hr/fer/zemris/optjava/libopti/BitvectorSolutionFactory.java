package hr.fer.zemris.optjava.libopti;

import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 */
public final class BitvectorSolutionFactory implements ISolutionFactory<BitvectorSolution> {
    private final BitvectorSolution mBitvectorInstance;
    private final Random mRandom;

    public BitvectorSolutionFactory(int bitCount, Random random) {
        mRandom = random;
        mBitvectorInstance = BitvectorSolution.create(bitCount);
    }

    @Override
    public BitvectorSolution newRandomized() {
        BitvectorSolution bitvectorSolution = mBitvectorInstance.duplicate();
        bitvectorSolution.randomize(mRandom);
        return bitvectorSolution;
    }

    @Override
    public BitvectorSolution newInstance() {
        return mBitvectorInstance.duplicate();
    }

    @Override
    public BitvectorSolution duplicate(BitvectorSolution instance) {
        return instance.duplicate();
    }

    @Override
    public BitvectorSolution[] newArray(int length) {
        return new BitvectorSolution[length];
    }

    @Override
    public BitvectorSolution[] newRandomizedArray(int length) {
        BitvectorSolution[] bitvectorSolutions = newArray(length);
        for (BitvectorSolution solution : bitvectorSolutions) {
            solution.randomize(mRandom);
        }
        return bitvectorSolutions;
    }

    @Override
    public BitvectorSolution[] duplicate(BitvectorSolution[] selectionCache) {
        BitvectorSolution[] bitvectorSolutions = newArray(selectionCache.length);
        for (int i = 0; i < selectionCache.length; i++) {
            bitvectorSolutions[i] = selectionCache[i].duplicate();
        }
        return bitvectorSolutions;
    }
}
