package hr.fer.zemris.ropaeruj.nsga;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
final class Chromosome implements Comparable<Chromosome> {
    public double[] values;
    public double fitness;
    public int eta;
    public List<Chromosome> S;
    public double[] evaluation;

    public Chromosome(int n) {
        values = new double[n];
    }

    public Chromosome newLikeThis() {
        return new Chromosome(values.length);

    }

    public void randomize(Random random, double[] lowerLimits, double[] upperLimits) {
        for (int i = 0; i < values.length; i++) {
            values[i] = lowerLimits[i] + (upperLimits[i] - lowerLimits[i]) * random.nextFloat();
        }
    }

    /**
     * @param other
     * @return 1, 0, -1 if solution is better, the same or worse than other respectively
     */
    @Override
    public int compareTo(Chromosome other) {
        if (fitness < other.fitness) {
            return -1;
        } else if (fitness == other.fitness) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        double[] a = values;
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder b = new StringBuilder();

        b.append(df.format(-fitness));
        b.append(" [");
        for (int i = 0; ; i++) {
            b.append(df.format(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }

    }


}
