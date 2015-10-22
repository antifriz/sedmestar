package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public abstract class BitvectorDecoder implements IDecoder<BitvectorSolution> {
    protected final double[] mMins;
    protected final double[] mMaxs;
    protected int[] mBits;
    protected int mN;
    int mTotalBits;

    protected BitvectorDecoder(double[] mins, double[] maxs, int[] _a, int _b) {
        mMins = mins;
        mMaxs = maxs;
    }

    public int getTotalBits() {
        return mTotalBits;
    }

    public int getDimensions() {
        return mN;
    }

    @Override
    public abstract double[] decode(BitvectorSolution object);

    @Override
    public abstract void decode(BitvectorSolution object, double[] decodedArray);
}
