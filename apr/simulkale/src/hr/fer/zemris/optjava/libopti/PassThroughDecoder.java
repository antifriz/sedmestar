package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public final class PassThroughDecoder implements IDecoder<DoubleArraySolution> {
    public PassThroughDecoder() {

    }

    @Override
    public double[] decode(DoubleArraySolution object) {
        return object.values;
    }

    @Override
    public void decode(DoubleArraySolution o, double[] arr) {
        assert o.values.length == arr.length;
        System.arraycopy(o.values, 0, arr, 0, arr.length);
    }
}
