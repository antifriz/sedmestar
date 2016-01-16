package hr.fer.zemris.optjava.rng.rngimpl;

import hr.fer.zemris.optjava.rng.IRNG;

import java.util.Random;

/**
 * Created by ivan on 1/16/16.
 */
public class JavaRandomImpl implements IRNG {

    private final Random mRandom;

    public JavaRandomImpl() {
        mRandom = new Random();
    }

    @Override
    public double nextDouble() {
        return mRandom.nextDouble();
    }

    @Override
    public float nextFloat() {
        return mRandom.nextFloat();
    }

    @Override
    public int nextInt() {
        return mRandom.nextInt();
    }

    @Override
    public boolean nextBoolean() {
        return mRandom.nextBoolean();
    }

    @Override
    public double nextGaussian() {
        return mRandom.nextGaussian();
    }
}
