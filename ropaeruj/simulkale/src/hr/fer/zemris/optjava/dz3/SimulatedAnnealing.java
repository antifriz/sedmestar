package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.*;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    private final IDecoder<T> mTIDecoder;
    private final INeighborhood<T> mTINeighborhood;
    private final T mTStartWith;
    private final IFunction mIFunction;
    private final boolean mMinimize;
    private final Random mRandom;
    private final ITempSchedule mTempSchedule;
    public boolean mWillLog;
    private T mBest;


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

        T current = mTStartWith;
        T currentBest = mTStartWith;
        calculateFitness(current);


        double nextTemperature = mTempSchedule.getNextTemperature();
        double initalTemperature = nextTemperature;
        for (int i = 0; i < mTempSchedule.getOuterLoopCounter(); i++) {

            mTINeighborhood.setFactor(nextTemperature / initalTemperature);

            if (mWillLog) {
                System.out.printf("[%5d - %8.6f] %f | %s || %f | %s\n", i, nextTemperature, Math.sqrt(currentBest.value), toString(currentBest), Math.sqrt(current.value), toString(current));
            }

            for (int j = 0; j < mTempSchedule.getInnerLoopCounter(); j++) {

                T neighbor = mTINeighborhood.randomNeighbor(current);
                calculateFitness(neighbor);
                if (neighbor.compareTo(current) >= 0 || willAcceptWorse(current, nextTemperature, neighbor)) {

                    current = neighbor;
                    if (current.compareTo(currentBest) >= 0) {
                        if (mWillLog) {

                            System.out.printf("        %f -> %f | %s\n", Math.sqrt(currentBest.value), Math.sqrt(current.value), toString(currentBest));
                        }
                        currentBest = current;
                    }
                }
            }
            nextTemperature = mTempSchedule.getNextTemperature();
        }

        mBest = currentBest;
    }

    private boolean willAcceptWorse(T current, double nextTemperature, T neighbor) {
        return mRandom.nextDouble() < Math.exp((neighbor.fitness - current.fitness) / nextTemperature);
    }

    public T getBest() {
        return mBest;
    }

    private void calculateFitness(T solution) {
        double v = valueAt(solution);
        solution.value = v;
        solution.fitness = mMinimize ? -v : v;
    }

    private double[] decode(T solution) {
        return mTIDecoder.decode(solution);
    }

    private double valueAt(T solution) {
        return mIFunction.valueAt(decode(solution));
    }

    private String toString(T solution) {
        return mTIDecoder.toString(solution);
    }
}
