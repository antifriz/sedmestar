package hr.fer.zemris.optjava.dz3;

/**
 * Created by ivan on 10/22/15.
 */
public class GeometricTempSchedule implements ITempSchedule {
    private double mAlpha;
    private double mTInitial;
    private double mTCurrent;
    private int mTInnerLimit;
    private int mTOuterLimit;

    public GeometricTempSchedule(double alpha, double tInitial, int tInnerLimit, int tOuterLimit) {
        mAlpha = alpha;
        mTInitial = tInitial;
        mTInnerLimit = tInnerLimit;
        mTOuterLimit = tOuterLimit;
    }

    @Override
    public double getNextTemperature() {
        return 0;
    }

    @Override
    public int getInnerLoopCounter() {
        return 0;
    }

    @Override
    public int getOuterLoopCounter() {
        return 0;
    }
}
