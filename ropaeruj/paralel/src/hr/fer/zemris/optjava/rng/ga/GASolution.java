package hr.fer.zemris.optjava.rng.ga;

/**
 * Created by ivan on 1/16/16.
 */
public abstract class GASolution<T> implements Comparable<GASolution<T>> {
    protected T data;
    public double fitness;

    public GASolution() {
    }

    public T getData() {
        return data;
    }

    public abstract GASolution<T> duplicate();

    @Override
    public int compareTo(GASolution<T> o) {
        return -Double.compare(this.fitness, o.fitness);
    }
}
