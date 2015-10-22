package hr.fer.zemris.optjava.libopti;


/**
 * Created by ivan on 10/22/15.
 */
public final class NaturalBinaryDecoder extends BitvectorDecoder {

    private NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public void decode(BitvectorSolution object, double... decodedArray) {
        for (int i = 0; i < mN; i++) {
            decodedArray[i] = interpolate(getRawBits(object.mBytes, i), i);
        }
    }
}
