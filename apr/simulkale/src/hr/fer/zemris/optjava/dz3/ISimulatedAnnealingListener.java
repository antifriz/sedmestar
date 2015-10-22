package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.IOptAlgorithmListener;
import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

/**
 * Created by ivan on 10/22/15.
 */
public interface ISimulatedAnnealingListener<T extends SingleObjectiveSolution> extends IOptAlgorithmListener<T> {
    default void onOuterStepStarted(T current, long progress) {
    }

    default void onOuterStepStopped(T current, long progress) {
    }
}
