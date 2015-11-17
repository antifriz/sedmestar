package hr.fer.zemris.ropaeruj.dz7;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 11/17/15.
 */
public class PSOTrainer implements IFANNTrainer {
    private static final boolean DEBUG = false;
    public static final double END_INERTIA = 0.9;
    public static final double BEGIN_INERTIA = 0.4;
    public static final int C_1 = 2;
    public static final int C_2 = 2;

    private final FFANN mFfann;
    private final IReadOnlyDataset mDataset;
    private final double mMaxWeightPosition;
    private final double mMaxWeightVelocity;
    private final int mWeightCount;
    private double mErr;
    private final int mMaxIter;
    private int mParticleCount;

    public PSOTrainer(FFANN ffann, IReadOnlyDataset dataset, double maxWeightPosition, double maxWeightVelocity, int particleCount, double err, int maxIter) {
        mFfann = ffann;
        mDataset = dataset;
        mMaxWeightPosition = maxWeightPosition;
        mMaxWeightVelocity = maxWeightVelocity;
        mErr = err;
        mMaxIter = maxIter;

        mParticleCount = particleCount;

        mWeightCount = ffann.getWeightsCount();
    }

    @Override
    public double[] trainFFANN() {
        Random random = new Random();

        Particle[] particles = new Particle[mParticleCount];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(mWeightCount, mMaxWeightPosition, mMaxWeightVelocity, random);
        }

        particles[0].setFitness(-evaluate(particles[0].x));
        Particle globalBest = new Particle(particles[0]);

        for (int i = 0; i < mMaxIter; i++) {
            evaluate(particles);

            for (Particle particle : particles) {
                if (particle.getFitness() > globalBest.getFitness()) {
                    globalBest = new Particle(particle);
                }
            }


            double inertia = calculateInertia(i, mMaxIter);
            for (Particle particle : particles) {
                particle.update(globalBest, inertia);
            }

            if (-globalBest.fitness < mErr) {
                break;
            }

            System.out.printf("[%5d] %3d%% %f\n", i, (int) Math.round(100 * percentageOfGoodClassifications(globalBest.x)), -globalBest.fitness);
        }
        System.out.printf("FIN %3d %f%% %s\n", (int) Math.round(100 * percentageOfGoodClassifications(globalBest.x)), -globalBest.fitness, Arrays.toString(globalBest.x));
        return globalBest.x;
    }

    private double calculateInertia(int iter, int maxIterCount) {
        return (1 - iter / (double) maxIterCount) * (END_INERTIA - BEGIN_INERTIA) + BEGIN_INERTIA;
    }

    private void evaluate(Particle[] particles) {
        if (DEBUG) System.out.println("Fitnesses:");
        for (Particle particle : particles) {
            particle.setFitness(-evaluate(particle.x));
            if (DEBUG) System.out.println(particle.getFitness());
        }
        if (DEBUG) System.out.println("Fitnesses end");
    }

    public double evaluate(double[] weights) {
        int outputDimension = mDataset.getOutputDimension();
        double[] outputs = new double[outputDimension];
        double sum = 0;
        for (double[][] sample : mDataset) {
            mFfann.calcOutputs(sample[0], weights, outputs);
            for (int i = 0; i < outputDimension; i++) {
                double diff = outputs[i] - sample[1][i];
                sum += diff * diff;
            }
        }
        return sum / mDataset.getSize();
    }


    private double percentageOfGoodClassifications(double[] weights) {
        int outputDimension = mDataset.getOutputDimension();
        double[] outputs = new double[outputDimension];
        double sum = 0;
        for (double[][] sample : mDataset) {
            int subsum = 0;
            mFfann.calcOutputs(sample[0], weights, outputs);
            for (int i = 0; i < outputDimension; i++) {
                if (Math.abs(outputs[i] - sample[1][i]) < 0.5) {
                    subsum++;
                }
            }
            if (subsum == outputDimension) {
                sum++;
            }
        }
        return sum / (double) mDataset.getSize();
    }


    private class Particle implements Comparable<Particle> {
        private final int mDim;
        private final double mMaxWeightVelocity;
        private final Random mRandom;
        double[] x;
        double[] v;
        private double fitness;
        double[] pb;

        Particle(int dim, double maxWeightPosition, double maxWeightVelocity, Random random) {
            mDim = dim;
            mMaxWeightVelocity = maxWeightVelocity;
            mRandom = random;
            x = random.doubles(dim).map(x -> (x * 2 - 1) * maxWeightPosition).toArray();
            v = random.doubles(dim).map(x -> (x * 2 - 1) * maxWeightVelocity).toArray();
            pb = x;
        }

        Particle(Particle particle) {
            mDim = particle.mDim;
            mMaxWeightVelocity = particle.mMaxWeightVelocity;
            mRandom = particle.mRandom;
            x = Arrays.copyOf(particle.x, particle.x.length);
            v = Arrays.copyOf(particle.v, particle.v.length);
            pb = Arrays.copyOf(particle.pb, particle.pb.length);
            fitness = particle.fitness;
        }

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            if (fitness > this.fitness) {
                pb = x;
            }
            this.fitness = fitness;
        }

        public void update(Particle socialParticle, double inertia) {
            for (int i = 0; i < mDim; i++) {
                v[i] = inertia * v[i] + C_1 * mRandom.nextDouble() * (pb[i] - x[i])
                        + C_2 * mRandom.nextDouble() * (socialParticle.x[i] - x[i]);
                v[i] = Math.max(-mMaxWeightVelocity, Math.min(mMaxWeightVelocity, v[i]));
                x[i] = x[i] + v[i];
            }
        }

        @Override
        public int compareTo(Particle o) {
            return Double.compare(fitness, o.fitness);
        }
    }
}
