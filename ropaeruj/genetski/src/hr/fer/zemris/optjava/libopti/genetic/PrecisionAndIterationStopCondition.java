package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

/**
 * Created by ivan on 10/31/15.
 */
public class PrecisionAndIterationStopCondition<T extends SingleObjectiveSolution> implements IStopCondition<T> {

    private final int mMaxIterAllowed;
    private final double mDesiredFitness;

    public PrecisionAndIterationStopCondition(int maxIterAllowed, double desiredFitness) {

        mMaxIterAllowed = maxIterAllowed;
        mDesiredFitness = desiredFitness;
    }

    @Override
    public boolean isSatisfied(int iterCount, T[] currentPopulation) {
        return iterCount <= mMaxIterAllowed && currentPopulation[0].value >= mDesiredFitness;
    }
}
