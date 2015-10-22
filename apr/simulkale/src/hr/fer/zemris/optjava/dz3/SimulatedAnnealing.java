package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.IDecoder;
import hr.fer.zemris.optjava.libopti.IFunction;
import hr.fer.zemris.optjava.libopti.INeighborhood;
import hr.fer.zemris.optjava.libopti.IOptAlgorithm;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public class SimulatedAnnealing<T> implements IOptAlgorithm<T> {

    private IDecoder<T> mTIDecoder;
    private INeighborhood<T> mTINeighborhood;
    private T mTStartWith;
    private IFunction mIFunction;
    private boolean mMinimize;
    private Random mRandom;

    public SimulatedAnnealing(IDecoder<T> tIDecoder, INeighborhood<T> tINeighborhood, IFunction IFunction, ITempSchedule schedule, boolean minimize) {
        mTIDecoder = tIDecoder;
        mTINeighborhood = tINeighborhood;
        mIFunction = IFunction;
        mMinimize = minimize;
    }

    @Override
    public void run() {

    }
}
