package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public interface IDecoder<T extends SingleObjectiveSolution> {
    double[] decode(T object);

    void decode(T object, double[] decodedArray);
}
