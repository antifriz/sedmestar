package hr.fer.zemris.optjava.rng.rngimpl;

import ec.util.MersenneTwisterFast;
import hr.fer.zemris.optjava.rng.IRNG;

import java.util.Random;

/**
 * Created by ivan on 1/16/16.
 */
public class MTFImpl implements IRNG {

    private final MersenneTwisterFast mRandom;

    public MTFImpl() {
        mRandom = new MersenneTwisterFast();
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
