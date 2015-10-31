package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

/**
 * Created by ivan on 10/31/15.
 */
public interface ISelector<T extends SingleObjectiveSolution> {
    T[] selection(T[] currentPopulation);
}
