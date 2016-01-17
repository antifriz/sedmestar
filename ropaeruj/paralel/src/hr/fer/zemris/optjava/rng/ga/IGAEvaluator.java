package hr.fer.zemris.optjava.rng.ga;

/**
 * Created by ivan on 1/16/16.
 */
public interface IGAEvaluator<T> {
    void evaluate(GASolution<T> p);
}
