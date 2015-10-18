package hr.fer.zemris.optjava.dz2;

import java.util.Random;

/**
 * Created by ivan on 10/18/15.
 */
public class RandomUtils {
    private static RandomUtils mInstance;
    private final Random mRandom;

    public static RandomUtils get() {
        if (mInstance == null) {
            mInstance = new RandomUtils();
        }
        return mInstance;
    }

    public RandomUtils() {
        mRandom = new Random();
    }

    public double nextDoubleInRange(double from, double to) {
        return mRandom.nextDouble() * (to - from) + from;
    }
}
