package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.IDecoder;
import hr.fer.zemris.optjava.libopti.IFunction;
import hr.fer.zemris.optjava.libopti.ISolutionFactory;
import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

/**
 * Created by ivan on 10/31/15.
 */
public abstract class GenerativeGeneticAlgorithm<T extends SingleObjectiveSolution> extends GeneticAlgorithm<T> {

    private T[] mNextPopulation;

    private int mElitismCount;

    public GenerativeGeneticAlgorithm(ISolutionFactory<T> solutionFactory, int populationSize, IFunction function, IDecoder<T> decoder, IStopCondition<T> stopCondition, boolean minimize, int elitismCount) {
        super(solutionFactory, populationSize, function, decoder, stopCondition, minimize);
        mElitismCount = elitismCount;

        mNextPopulation = solutionFactory.newArray(populationSize);
    }


    @Override
    protected final void updatePopulation() {
        promoteBest();

        for (int i = mElitismCount; i < mNextPopulation.length; ) {

            mNextPopulation[i] = createChild();
        }
        T[] k = mCurrentPopulation;
        mCurrentPopulation = mNextPopulation;
        mNextPopulation = k;
    }

    protected abstract T createChild();

    private void promoteBest() {
        for (int i = 0; i < mElitismCount; i++) {
            mNextPopulation[i] = mSolutionFactory.duplicate(mCurrentPopulation[i]);
        }
    }
}
