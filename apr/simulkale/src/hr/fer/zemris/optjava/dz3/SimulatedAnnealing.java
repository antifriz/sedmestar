package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.*;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T, ISimulatedAnnealingListener<T>> {

    private final IDecoder<T> mTIDecoder;
    private final INeighborhood<T> mTINeighborhood;
    private final T mTStartWith;
    private final IFunction mIFunction;
    private final boolean mMinimize;
    private final Random mRandom;
    private final ITempSchedule mTempSchedule;
    private final ISimulatedAnnealingListener<T> mTListener;

    private T mCurrentBest;

    public SimulatedAnnealing(IDecoder<T> tIDecoder, INeighborhood<T> tINeighborhood, T tStartWith, IFunction iFunction, ITempSchedule schedule, boolean minimize, ISimulatedAnnealingListener<T> tListener) {
        mTIDecoder = tIDecoder;
        mTINeighborhood = tINeighborhood;
        mTStartWith = tStartWith;
        mIFunction = iFunction;
        mMinimize = minimize;
        mTempSchedule = schedule;
        mTListener = tListener;
        mRandom = new Random();
        mCurrentBest = mTStartWith;
    }

    @Override
    public void run() {
        mTempSchedule.reset();

        T current = mTStartWith;
        mTListener.onSolutionChanged(null, current, 0);
        calculateFitness(current);
        updateBest(current);

        for (int i = 0; i < mTempSchedule.getOuterLoopCounter(); i++) {

            mTListener.onOuterStepStarted(current, i);

            for (int j = 0; j < mTempSchedule.getInnerLoopCounter(); j++) {

                mTListener.onStepStarted(current, j);

                T neighbor = mTINeighborhood.randomNeighbor(current);
                calculateFitness(neighbor);
                if (willAccept(current, neighbor)) {

                    mTListener.onSolutionChanged(current, neighbor, j);

                    current = neighbor;
                    updateBest(current);
                }

                mTListener.onStepStopped(current, j);

            }

            mTListener.onOuterStepStopped(current, i);

        }
    }

    public T getCurrentBest() {
        return mCurrentBest;
    }

    private void calculateFitness(T current) {
        double v = mIFunction.valueAt(mTIDecoder.decode(current));
        current.value = mMinimize ? -v : v;
    }

    private boolean willAccept(T current, T neighbor) {
        return isBetter(neighbor, current) || willAcceptWorse();
    }

    private boolean isBetter(T first, T second) {
        return first.compareTo(second) >= 0;
    }


    private boolean willAcceptWorse() {
        return mRandom.nextDouble() < mTempSchedule.getNextTemperature();
    }

    private void updateBest(T current) {
        if (current.compareTo(mCurrentBest) >= 0) {
            mTListener.onBestChanged(mCurrentBest, current, 0);
            mCurrentBest = current;
        }
    }
}
