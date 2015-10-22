package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public interface INeighborhood<T> {
    T randomNeighbor(T o);
}
