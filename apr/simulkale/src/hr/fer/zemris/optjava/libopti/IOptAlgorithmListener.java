package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public interface IOptAlgorithmListener<T extends SingleObjectiveSolution> {
    default void onStepStarted(T current, long progress) {
    }

    default void onStepStopped(T current, long progress) {
    }

    void onSolutionChanged(T oldSolution, T newSolution, long progress);

    default void onBestChanged(T oldBest, T newBest, long progress) {
    }
}
