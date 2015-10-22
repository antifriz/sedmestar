package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

/**
 * Created by ivan on 10/22/15.
 */
public class VoidSimulatedAnnealingListener<T extends SingleObjectiveSolution> implements ISimulatedAnnealingListener<T> {
    @Override
    public void onSolutionChanged(T oldSolution, T newSolution, long progress) {

    }
}
