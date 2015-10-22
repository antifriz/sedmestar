package hr.fer.zemris.optjava.dz3;

/**
 * Created by ivan on 10/22/15.
 */
public class GeometricTempSchedule implements ITempSchedule {
    private final double mAlpha;
    private final double mTInitial;
    private final int mTInnerLimit;
    private final int mTOuterLimit;
    private double mTCurrent;

    public GeometricTempSchedule(double alpha, double tInitial, int tInnerLimit, int tOuterLimit) {
        mAlpha = alpha;
        mTInitial = tInitial;
        mTInnerLimit = tInnerLimit;
        mTOuterLimit = tOuterLimit;
    }

    @Override
    public final void reset() {
        mTCurrent = mTInitial / mAlpha;
    }

    @Override
    public double getNextTemperature() {
        return mTCurrent *= mAlpha;
    }

    @Override
    public int getInnerLoopCounter() {
        return mTInnerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return mTOuterLimit;
    }
}
