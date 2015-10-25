package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public final class GreyBinaryDecoder extends BitvectorDecoder {
    public GreyBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    private static long decodeGray(long n) {
        long p = n;
        while ((n >>>= 1) != 0) {
            p ^= n;
        }
        return p;
    }

    @Override
    public void decode(BitvectorSolution object, double... decodedArray) {
        for (int i = 0; i < mN; i++) {
            long n = getRawBits(object.mBytes, i);
            decodedArray[i] = interpolate(decodeGray(n), i);
        }
    }
}
