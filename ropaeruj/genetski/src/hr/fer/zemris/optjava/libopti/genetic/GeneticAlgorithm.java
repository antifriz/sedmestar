package hr.fer.zemris.optjava.libopti.genetic;

import hr.fer.zemris.optjava.libopti.IDecoder;
import hr.fer.zemris.optjava.libopti.IFunction;
import hr.fer.zemris.optjava.libopti.IOptAlgorithm;
import hr.fer.zemris.optjava.libopti.SingleObjectiveSolution;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 */
public abstract class GeneticAlgorithm<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    protected T[] mCurrentPopulation;
    protected int mPopulationSize;
    private IFunction mFunction;
    private IDecoder<T> mDecoder;

    public GeneticAlgorithm(T[] initialPopulation, IFunction function, IDecoder<T> decoder) {
        mFunction = function;
        mDecoder = decoder;
        mCurrentPopulation = Arrays.copyOf(initialPopulation, initialPopulation.length);
        mPopulationSize = mCurrentPopulation.length;
    }

    @Override
    public final void run() {

        while (true) {
            evaluate();
            if (isStopConditionSatisfied()) {
                break;
            }
            updatePopulation();
        }
    }

    public T rouletteWheelSelection(T[] array, Random rand){
        assert Arrays.stream(array).allMatch(x->x.fitness>=0);

        double sum = Arrays.stream(array).mapToDouble(x->x.fitness).sum();
        double roulletePick = rand.nextDouble()*sum;

        double it = 0;
        for (T anArray : array) {
            it += anArray.fitness;
            if (it > roulletePick) {
                return anArray;
            }
        }
        throw new InvalidStateException("");
    }

    private void evaluate() {
        Arrays.stream(mCurrentPopulation).parallel().forEach(this::evaluate);
    }

    protected final void evaluate(T solution) {
        solution.fitness = mFunction.valueAt(mDecoder.decode(solution));
    }

    protected abstract void updatePopulation();

    public abstract boolean isStopConditionSatisfied();
}
