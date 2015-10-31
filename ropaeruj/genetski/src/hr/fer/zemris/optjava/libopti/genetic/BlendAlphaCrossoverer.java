package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.DoubleArraySolution;
import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 */
public class BlendAlphaCrossoverer<T extends SingleObjectiveSolution> implements ICrossoverer<DoubleArraySolution> {
    private Random mRandom;

    public BlendAlphaCrossoverer(Random random) {
        mRandom = random;
    }

    @Override
    public void crossover(DoubleArraySolution[] parents) {
        assert parents.length == 2;

    }
}
