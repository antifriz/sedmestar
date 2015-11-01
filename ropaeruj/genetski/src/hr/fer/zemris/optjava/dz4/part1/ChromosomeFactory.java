package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 */
final class ChromosomeFactory {
    private final Chromosome mChromosome;
    private final Random mRandom;
    private final double[] mLower;
    private final double[] mUpper;

    public ChromosomeFactory(int dimSize, Random random, double[] lower, double[] upper) {
        mRandom = random;
        mLower = lower;
        mUpper = upper;
        mChromosome = new Chromosome(dimSize);
    }
    public Chromosome newInstance() {
        return mChromosome.duplicate();
    }

    public Chromosome[] newArray(int length) {
        Chromosome[] chromosomes = new Chromosome[length];
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = newInstance();
        }
        return chromosomes;
    }

    public Chromosome[] newRandomizedArray(int length) {
        Chromosome[] chromosomes = newArray(length);
        for (Chromosome solution : chromosomes) {
            solution.randomize(mRandom, mLower, mUpper);
        }
        return chromosomes;
    }

}
