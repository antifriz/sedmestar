package hr.fer.zemris.apr.ga;

/**
 * Created by ivan on 10/22/15.
 */
public interface INeighborhood<T extends SingleObjectiveSolution> {
    T randomNeighbor(T solution);

    void setFactor(double factor);
}
