package hr.fer.zemris.optjava.libopti;

import java.util.Arrays;

/**
 * Created by ivan on 10/22/15.
 */
public abstract class BitvectorDecoder implements IDecoder<BitvectorSolution> {
    protected final double[] mMins;
    protected final double[] mMaxs;
    protected final int[] mBits;
    protected final int mN;
    private final int[] mOffsets;
    int mTotalBits;

    protected BitvectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        if (mins.length != n || maxs.length != n || bits.length != n) {
            throw new IllegalArgumentException("mins, maxs and bits must be the same size as n");
        }

        mMins = Arrays.copyOf(mins, mins.length);
        mMaxs = Arrays.copyOf(maxs, maxs.length);
        mBits = Arrays.copyOf(bits, bits.length);
        mOffsets = new int[mBits.length];
        mN = n;
        int bitCount = 0;
        for (int i = 0; i < bits.length; i++) {
            int bit = bits[i];
            mOffsets[i] = bitCount;
            bitCount += bit;
        }
        mTotalBits = bitCount;
    }


    protected final long getRawBits(byte[] bytes, int componentIdx) {
        int from = mOffsets[componentIdx];
        int to = from + mBits[componentIdx];
        long rawBits = 0;
        for (int i = from; i < to; i++) {
            rawBits <<= 1;
            int bucket = i / Byte.SIZE;
            int shift = Byte.SIZE - 1 - (i % Byte.SIZE);
            byte bucketVal = bytes[(bucket)];
            int val = bucketVal >> shift;
            rawBits += val & 1;
        }
        return rawBits;
    }

    protected final double interpolate(long value, int componentIdx) {
        return mMins[componentIdx] + value / Math.pow(2, mBits[componentIdx]) * (mMaxs[componentIdx] - mMins[componentIdx]);
    }

    @Override
    public double[] decode(BitvectorSolution object) {
        double[] arr = new double[mN];
        decode(object, arr);
        return arr;
    }

    @Override
    public abstract void decode(BitvectorSolution object, double... decodedArray);
}
