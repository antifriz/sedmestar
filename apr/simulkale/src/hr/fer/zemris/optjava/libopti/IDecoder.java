package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public interface IDecoder<T> {
    double[] decode(T o);

    void decode(T o, double[] arr);
}
