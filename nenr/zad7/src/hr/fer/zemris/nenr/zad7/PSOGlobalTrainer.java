package hr.fer.zemris.nenr.zad7;

/**
 * Created by ivan on 11/17/15.
 */
public final class PSOGlobalTrainer extends PSOTrainer {


    public PSOGlobalTrainer(FFANN ffann, int particleCount, double err, int maxIter) {
        super(ffann, particleCount, err, maxIter);
    }




    @Override
    protected PSOTrainer.Particle getSocialParticle(int idx) {
        return mGlobalBest;
    }
}
