package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

/**
 * Created by ivan on 10/22/15.
 */
public class VerboseSimulatedAnnealingListener<T extends SingleObjectiveSolution> implements ISimulatedAnnealingListener<T> {
    @Override
    public void onStepStarted(T current, long progress) {

    }

    @Override
    public void onStepStopped(T current, long progress) {

    }

    @Override
    public void onSolutionChanged(T oldSolution, T newSolution, long progress) {

    }

    @Override
    public void onBestChanged(T oldBest, T newBest, long progress) {

    }

    @Override
    public void onOuterStepStarted(T current, long progress) {

    }

    @Override
    public void onOuterStepStopped(T current, long progress) {

    }
}
