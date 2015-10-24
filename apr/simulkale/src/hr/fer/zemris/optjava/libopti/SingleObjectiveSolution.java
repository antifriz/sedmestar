package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public abstract class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {
    public double fitness;
    public double value;

    public SingleObjectiveSolution() {
    }

    /**
     * @param other
     * @return 1, 0, -1 if solution is better, the same or worse than other respectively
     */
    @Override
    public int compareTo(SingleObjectiveSolution other) {
        if (fitness < other.fitness) {
            return -1;
        } else if (fitness == other.fitness) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public abstract String toString();
}
