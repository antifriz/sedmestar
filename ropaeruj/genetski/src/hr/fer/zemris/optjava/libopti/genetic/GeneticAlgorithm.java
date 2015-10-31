package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.*;

import java.util.Arrays;

/**
 * Created by ivan on 10/31/15.
 */
public abstract class GeneticAlgorithm<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    protected T[] mCurrentPopulation;
    protected int mPopulationSize;
    private IFunction mFunction;
    private IDecoder<T> mDecoder;
    private IStopCondition<T> mStopCondition;
    private boolean mMinimize;
    protected final ISolutionFactory<T> mSolutionFactory;

    public GeneticAlgorithm(ISolutionFactory<T> solutionFactory, int populationSize, IFunction function, IDecoder<T> decoder, IStopCondition<T> stopCondition, boolean minimize) {
        mSolutionFactory = solutionFactory;
        mPopulationSize = populationSize;
        mFunction = function;
        mDecoder = decoder;
        mStopCondition = stopCondition;

        mMinimize = minimize;
    }

    @Override
    public final void run() {
        mCurrentPopulation = mSolutionFactory.newRandomizedArray(mPopulationSize);

        for (int i = 0; ; i++) {
            evaluate();
            if (mStopCondition.isSatisfied(i, mCurrentPopulation)) {
                break;
            }
            updatePopulation();
        }
    }

    private void evaluate() {
        Arrays.stream(mCurrentPopulation).parallel().forEach(this::evaluate);
    }

    protected final void evaluate(T solution) {
        solution.value = mFunction.valueAt(mDecoder.decode(solution));
        solution.fitness = mMinimize ? -solution.value : solution.value;
        Arrays.sort(mCurrentPopulation);
    }

    protected abstract void updatePopulation();

}
