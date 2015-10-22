package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public interface IOptAlgorithm<T extends SingleObjectiveSolution, U extends IOptAlgorithmListener<T>> {
    void run();
}
