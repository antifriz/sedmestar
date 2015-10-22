package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public interface INeighborhood<T extends SingleObjectiveSolution> {
    T randomNeighbor(T solution);
}
