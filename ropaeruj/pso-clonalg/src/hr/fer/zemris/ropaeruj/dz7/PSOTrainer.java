package hr.fer.zemris.ropaeruj.dz7;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 11/17/15.
 */
public abstract class PSOTrainer implements IFANNTrainer {
    public static final double END_INERTIA = 0.9;
    public static final double BEGIN_INERTIA = 0.4;
    public static final int C_1 = 2;
    public static final int C_2 = 2;
    public static final double MAX_VELOCITY = 1;
    public static final double MAX_POSITION = 1;

    private final FFANN mFfann;
    private final int mWeightCount;
    private double mErr;
    private final int mMaxIter;
    private int mParticleCount;
     Particle[] mParticles;
    Particle mGlobalBest;

    public PSOTrainer(FFANN ffann, int particleCount, double err, int maxIter) {
        mFfann = ffann;
        mErr = err;
        mMaxIter = maxIter;

        mParticleCount = particleCount;

        mWeightCount = ffann.getWeightsCount();
    }

    @Override
    public double[] trainFFANN() {

        initParticles();

        for (int i = 0; i < mMaxIter; i++) {
            evaluate(mParticles);

            updateBest();

            double inertia = calculateInertia(i, mMaxIter);
            for (int i1 = 0; i1 < mParticles.length; i1++) {
               mParticles[i1].update(getSocialParticle(i1), inertia);
            }

            if (-mGlobalBest.fitness < mErr) {
                break;
            }

            System.out.printf("[%5d] %3d%% %f\n", i, (int) Math.round(100 * mFfann.percentageOfGoodClassifications(mGlobalBest.x)), -mGlobalBest.fitness);
        }
        System.out.printf("FIN %3d %f%% %s\n", (int) Math.round(100 * mFfann.percentageOfGoodClassifications(mGlobalBest.x)), -mGlobalBest.fitness, Arrays.toString(mGlobalBest.x));
        return mGlobalBest.x;
    }

    protected void updateBest() {
        for (Particle particle : mParticles) {
            if (particle.getFitness() > mGlobalBest.getFitness()) {
                mGlobalBest = new Particle(particle);
            }
        }
    }

    protected void initParticles() {
        Random random = new Random();

        mParticles = new Particle[mParticleCount];
        for (int i = 0; i < mParticles.length; i++) {
            mParticles[i] = new Particle(mWeightCount, random);
        }

        evaluate(mParticles);

        mGlobalBest = new Particle(mParticles[0]);
    }

    protected abstract Particle getSocialParticle(int idx);

    private double calculateInertia(int iter, int maxIterCount) {
        return (1 - iter / (double) maxIterCount) * (END_INERTIA - BEGIN_INERTIA) + BEGIN_INERTIA;
    }

    private void evaluate(Particle[] particles) {
        for (Particle particle : particles) {
            particle.setFitness(-mFfann.evaluate(particle.x));
        }
    }




    protected class Particle implements Comparable<Particle> {
        private final int mDim;
        private final Random mRandom;
        final double[] x;
        final double[] v;
        private double fitness;
        double[] pb;

        Particle(int dim, Random random) {
            mDim = dim;
            mRandom = random;
            x = random.doubles(dim).map(x -> (x * 2 - 1) * MAX_POSITION).toArray();
            v = random.doubles(dim).map(x -> (x * 2 - 1) * MAX_VELOCITY).toArray();
            pb = x;
        }

        Particle(Particle particle) {
            mDim = particle.mDim;
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
                v[i] = Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, v[i]));
                x[i] = x[i] + v[i];
            }
        }

        @Override
        public int compareTo(Particle o) {
            return Double.compare(fitness, o.fitness);
        }
    }
}
