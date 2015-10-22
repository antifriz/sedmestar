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

    private final IDecoder<T> mTIDecoder;
    private final INeighborhood<T> mTINeighborhood;
    private final T mTStartWith;
    private final IFunction mIFunction;
    private final boolean mMinimize;
    private final Random mRandom;
    private final ITempSchedule mTempSchedule;

    private T mCurrentBest;

    public SimulatedAnnealing(IDecoder<T> tIDecoder, INeighborhood<T> tINeighborhood, T tStartWith, IFunction iFunction, ITempSchedule schedule, boolean minimize) {
        mTIDecoder = tIDecoder;
        mTINeighborhood = tINeighborhood;
        mTStartWith = tStartWith;
        mIFunction = iFunction;
        mMinimize = minimize;
        mTempSchedule = schedule;
        mRandom = new Random();
    }

    @Override
    public void run() {
        mTempSchedule.reset();

        T currentBest;

        for (int i = 0; i < mTempSchedule.getOuterLoopCounter(); i++) {
            for (int j = 0; j < mTempSchedule.getInnerLoopCounter(); j++) {

            }
        }
    }
}
