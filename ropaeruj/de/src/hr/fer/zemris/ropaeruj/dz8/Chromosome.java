package hr.fer.zemris.ropaeruj.dz8;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 12/9/15.
 */
public class Chromosome {
    public double[] genes;
    public double error;

    private Chromosome(double[] genes) {
        this.genes = genes;
    }

    public static Chromosome createNormalizedRandom(Random random, int dimension) {
        return new Chromosome(random.doubles(-1, 1).limit(dimension).toArray());
    }

    public Chromosome duplicate() {
        return new Chromosome(Arrays.copyOf(genes, genes.length));
    }
}
