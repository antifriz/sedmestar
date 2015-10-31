package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/31/15.
 */
public interface ISolutionFactory <T extends SingleObjectiveSolution> {
    T newRandomized();
    T newInstance();
    T duplicate(T instance);

    T[] newArray(int length);
    T[] newRandomizedArray(int length);

    T[] duplicate(T[] selectionCache);
}
