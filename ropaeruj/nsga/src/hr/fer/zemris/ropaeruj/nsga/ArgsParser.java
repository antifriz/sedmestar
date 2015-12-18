package hr.fer.zemris.ropaeruj.nsga;


/**
 * Created by ivan on 10/23/15.
 */
final class ArgsParser {
    private final int mPopulationCount;
    private final int mMaxIterCount;
    private final double mMutationSigma;
    private final int mProblemIdx;
    private final double mShareSigma;
    private final double mEpsilon;
    private final boolean mUseSolutionSpaceDensity;

    public ArgsParser(String[] args) {
        if (args.length !=6) {
            System.err.println("Parameters: problem_idx pop_size {decision-space|objective-space} maxiter share_sigma epsilon");
            System.exit(1);
        }

        mProblemIdx = Integer.valueOf(args[0]);

        mPopulationCount = Integer.valueOf(args[1]);

        mUseSolutionSpaceDensity = true;

        mMaxIterCount = Integer.valueOf(args[3]);

        mShareSigma = 0.1;

        mEpsilon = 0.01;

        mMutationSigma = mShareSigma;

    }

    public Integer getPopulationCount() {
        return mPopulationCount;
    }

    public Integer getMaxIterCount() {
        return mMaxIterCount;
    }

    public Double getMutationSigma() {
        return mMutationSigma;
    }

    public double getShareSigma() {
        return mShareSigma;
    }

    public double getEpsilon() {
        return mEpsilon;
    }

    public boolean useSolutionSpaceDensity() {
        return mUseSolutionSpaceDensity;
    }

    public int getProblemIdx() {
        return mProblemIdx;
    }
}
