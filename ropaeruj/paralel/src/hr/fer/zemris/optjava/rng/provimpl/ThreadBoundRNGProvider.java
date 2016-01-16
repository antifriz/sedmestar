package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ivan on 1/16/16.
 */
public class ThreadBoundRNGProvider implements IRNGProvider {
    private IRNGProvider provider;

    public ThreadBoundRNGProvider(IRNGProvider provider) {
        this.provider = provider;
    }

    @Override
    public IRNG getRNG() {
        return provider.getRNG();
    }
}
