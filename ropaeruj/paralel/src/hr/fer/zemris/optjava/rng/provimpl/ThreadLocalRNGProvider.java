package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Created by ivan on 1/16/16.
 */
public class ThreadLocalRNGProvider implements IRNGProvider {
    ThreadLocal<IRNG> mThreadLocalRNG = new ThreadLocal<>();

    @Override
    public IRNG getRNG() {
        if (mThreadLocalRNG.get() == null) {
            mThreadLocalRNG.set(RNG.getIRNGInstance());
        }
        return mThreadLocalRNG.get();
    }
}
