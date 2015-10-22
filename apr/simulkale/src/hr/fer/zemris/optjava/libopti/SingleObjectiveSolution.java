package hr.fer.zemris.optjava.libopti;

/**
 * Created by ivan on 10/22/15.
 */
public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {
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
        return 0;
    }
}
