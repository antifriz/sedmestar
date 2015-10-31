package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.IDecoder;
import hr.fer.zemris.optjava.libopti.IFunction;
import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;

import java.util.Arrays;

/**
 * Created by ivan on 10/31/15.
 */
public abstract class GenerativeGeneticAlgorithm<T extends SingleObjectiveSolution> extends GeneticAlgorithm<T> {

    private T[] mNextPopulation;

    private ISelector<T> mSelector;
    private ICrossoverer<T> mCrossoverer;
    private IMutator<T> mMutator;

    private int mElitismCount;

    public GenerativeGeneticAlgorithm(T[] initialPopulation, IFunction function, IDecoder<T> decoder) {
        super(initialPopulation, function, decoder);
        mNextPopulation = Arrays.copyOf(initialPopulation, initialPopulation.length);
    }

    @Override
    protected void updatePopulation() {
        promoteBest();

        for (int i = mElitismCount; ; ) {
            T[] parents = mSelector.selection(mCurrentPopulation);
            assert parents.length == 2;
            T[] children = mCrossoverer.crossover(parents);

            assert children.length == 2;

            mMutator.mutate(children);

            if (i == mNextPopulation.length) {
                break;
            }
            mNextPopulation[i++] = children[0];
            if (i == mNextPopulation.length) {
                break;
            }
            mNextPopulation[i++] = children[1];
        }
    }

    private void promoteBest() {
        if (mElitismCount != 0) {
            Arrays.sort(mCurrentPopulation, (a, b) -> Double.compare(a.fitness, b.fitness));

            System.arraycopy(mCurrentPopulation, 0, mNextPopulation, 0, mElitismCount);
            //mNextPopulation[0] = Arrays.stream(mCurrentPopulation).max((a, b) -> Double.compare(a.fitness, b.fitness)).get();
        }
    }


}
