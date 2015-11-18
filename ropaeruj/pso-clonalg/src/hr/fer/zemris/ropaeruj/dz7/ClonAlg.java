package hr.fer.zemris.ropaeruj.dz7;

import java.util.*;

/**
 * Created by ivan on 11/17/15.
 */
public final class ClonAlg implements IFANNTrainer {

    private final FFANN mFfann;
    private final int mPopulationCount;
    private final int mMaxIter;
    private static final double VALUE_RANGE = 1;
    private double mErr;

    public ClonAlg(FFANN ffann, int populationCount, double err, int maxIter) {

        mFfann = ffann;
        mPopulationCount = populationCount;
        mMaxIter = maxIter;
        mErr = err;
    }

    @Override
    public double[] trainFFANN() {

        Random random = new Random();

        int populationCount = mPopulationCount;
        Antibody[] antibodies = createNew(random, populationCount);

        int n = mPopulationCount;//to select for cloning
        double beta = 1;
        int d = (int) (n * 0.1);


        for (int i = 0; i < mMaxIter; i++) {
            evaluate(antibodies);

            antibodies = pick(antibodies, n);
            antibodies = clone(antibodies, beta);
            hypermutate(antibodies);

            evaluate(antibodies);

            antibodies = pick(antibodies, n);

            Antibody[] antibodiesBirth = createNew(random, d);

            replace(antibodies, antibodiesBirth);
            System.out.printf("[%5d] %3d%% %f\n", i, (int) Math.round(100 * mFfann.percentageOfGoodClassifications(antibodies[0].values)), -antibodies[0].fitness);

            if (-antibodies[0].fitness < mErr) {
                break;
            }
        }
        System.out.printf("FIN %3d %f%% %s\n", (int) Math.round(100 * mFfann.percentageOfGoodClassifications(antibodies[0].values)), -antibodies[0].fitness, Arrays.toString(antibodies[0].values));

        return antibodies[0].values;
    }

    private void replace(Antibody[] antibodies, Antibody[] antibodiesBirth) {
        System.arraycopy(antibodiesBirth, 0, antibodies, antibodies.length - antibodiesBirth.length, antibodiesBirth.length);
    }

    private void hypermutate(Antibody[] antibodies) {
        double min = antibodies[0].fitness;
        double max = antibodies[0].fitness;
        int length = antibodies.length;
        for (int i = 1; i < length; i++) {
            double fitness = antibodies[i].fitness;
            min = Math.min(fitness, min);
            max = Math.max(fitness, max);
        }

        for (Antibody antibody : antibodies) {
            antibody.hypermutate(min, max);
        }
    }

    private Antibody[] clone(Antibody[] antibodies, double beta) {
        List<Antibody> cloned = new ArrayList<>((int) (antibodies.length * beta / 2));
        int n = antibodies.length;
        for (int i = 0; i < n; i++) {
            int nc = (int) Math.floor(beta * n / (i + 1));
            Antibody copy = antibodies[i].copy();
            copy.tweak = true;
            cloned.add(copy);
            for (int j = 1; j < nc; j++) {
                cloned.add(antibodies[i].copy());
            }
        }
        return cloned.toArray(new Antibody[cloned.size()]);
    }

    private Antibody[] pick(Antibody[] antibodies, int n) {
        Arrays.sort(antibodies, Comparator.<Antibody>reverseOrder());
        return Arrays.copyOf(antibodies, n);
    }

    private Antibody[] createNew(Random random, int populationCount) {
        Antibody[] antibodies = new Antibody[populationCount];
        for (int i = 0; i < antibodies.length; i++) {
            antibodies[i] = new Antibody(mFfann.getWeightsCount(), random);
        }
        return antibodies;
    }

    private void evaluate(Antibody[] antibodies) {
        Arrays.stream(antibodies).parallel().filter(x->!x.tweak).forEach(antibody->antibody.fitness = -mFfann.evaluate(antibody.values));
    }

    private static class Antibody implements Comparable<Antibody> {
        private static final double RHO = 20;
        private final Random mRandom;
        double[] values;
        double fitness;
        boolean tweak;

        Antibody(int dim, Random random) {
            mRandom = random;
            this.values = random.doubles(dim).map(x -> (2 * x - 1) * VALUE_RANGE).toArray();
        }

        private Antibody(double[] values, double fitness, Random random) {
            this.values = values;
            this.fitness = fitness;
            mRandom = random;
        }

        @Override
        public int compareTo(Antibody o) {
            return Double.compare(this.fitness, o.fitness);
        }

        public Antibody copy() {
            return new Antibody(Arrays.copyOf(values, values.length), fitness, mRandom);
        }

        public void hypermutate(double min, double max) {
            if (!tweak) {
                double quality = fitness;//(fitness - min) / (max - min);
                double odds = Math.exp(-quality * RHO) ;
                odds = 1/(double)values.length;

                int length = values.length;
                for (int i = 0; i < length; i++) {
                    values[i] += odds * mRandom.nextGaussian();
                }
            } else {
                tweak = false;
            }
        }
    }
}
