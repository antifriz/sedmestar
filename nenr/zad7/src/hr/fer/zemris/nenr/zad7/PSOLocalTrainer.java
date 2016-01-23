package hr.fer.zemris.nenr.zad7;

/**
 * Created by ivan on 11/17/15.
 */
public final class PSOLocalTrainer extends PSOTrainer {
    private final int mNeighborhoodSize;
    private Particle[][] mNeighbors;
    private Particle[] mBestNeighbors;

    public PSOLocalTrainer(FFANN ffann, int particleCount, double err, int maxIter, int neighborhoodSize) {
        super(ffann, particleCount, err, maxIter);
        mNeighborhoodSize = neighborhoodSize;
        mBestNeighbors = new Particle[particleCount];
        mNeighbors = new Particle[particleCount][mNeighborhoodSize * 2 + 1];
    }


    @Override
    protected void initParticles() {
        super.initParticles();
        for (int i = 0; i < mParticles.length; i++) {
            for (int j = 0; j < mNeighborhoodSize * 2 + 1; j++) {
                mNeighbors[i][j] = mParticles[(mParticles.length * 2 - mNeighborhoodSize+j) % mParticles.length];
            }
            mBestNeighbors[i] = new Particle(mParticles[i]);
        }
    }

    @Override
    protected void updateBest() {
        super.updateBest();
        for (int i = 0; i < mBestNeighbors.length; i++) {
            Particle[] neighborParticles = mNeighbors[i];
            for (int j = 0; j < mNeighborhoodSize * 2 + 1; j++) {
                if(neighborParticles[j].getFitness() > mBestNeighbors[i].getFitness()){
                    mBestNeighbors[i] = new Particle(neighborParticles[j]);
                }
            }
        }
    }

    @Override
    protected Particle getSocialParticle(int idx) {
        return mBestNeighbors[idx];
    }


}