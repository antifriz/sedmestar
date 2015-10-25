package hr.fer.zemris.optjava.dz3;

/**
 * Created by ivan on 10/24/15.
 */
public class Config {
    private final double mDeltas;
    private final double mAlpha;
    private final double mInitial;
    private final int mOuter;
    private final int mInner;

    public Config(double deltas, double alpha, double initial, double outer, double inner) {
        mDeltas = deltas;
        mAlpha = alpha;
        mInitial = initial;
        mOuter = (int) Math.round(outer);
        mInner = (int) Math.round(inner);
    }

    public double getDeltas() {
        return mDeltas;
    }

    public double getAlpha() {
        return mAlpha;
    }

    public double getInitial() {
        return mInitial;
    }

    public int getOuter() {
        return mOuter;
    }

    public int getInner() {
        return mInner;
    }
}
