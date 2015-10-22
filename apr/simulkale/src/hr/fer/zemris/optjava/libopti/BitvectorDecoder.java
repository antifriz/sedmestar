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
    private final int[] mBucketFrom;
    private final int[] mBucketTo;
    int mTotalBits;

    protected BitvectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        if (mins.length != n || maxs.length != n || bits.length != n) {
            throw new IllegalArgumentException("mins, maxs and bits must be the same size as n");
        }

        mMins = Arrays.copyOf(mins, mins.length);
        mMaxs = Arrays.copyOf(maxs, maxs.length);
        mBits = Arrays.copyOf(bits, bits.length);
        mN = n;
        int bitCount = 0;
        for (int bit : bits) {
            bitCount += bit;
        }
        mTotalBits = bitCount;

        mBucketFrom = new int[n];
        mBucketTo = new int[n];
        mBucketTo[0] = mBits[0];
        mBucketFrom[0] = 0;
        for (int i = 1; i < n; i++) {
            int dim = bits[i];
            if (dim > Double.SIZE) {
                throw new IllegalArgumentException("Every bitvector dimension must not be larger than " + Double.SIZE + " bits");
            }
            mBucketTo[i] = mBucketTo[i - 1] + (dim + 7) >> 3;
            if (i + 1 < n) {
                mBucketFrom[i + 1] = mBucketTo[i];
            }
        }
    }

    protected final long getRawBits(byte[] bytes, int componentIdx) {
        long rawBits = 0;
        for (int i = mBucketFrom[componentIdx]; i < mBucketTo[componentIdx]; i++) {
            rawBits <<= Byte.SIZE;
            rawBits += bytes[i];
        }
        rawBits &= 0x1 >> Byte.SIZE - mBits[componentIdx];
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
