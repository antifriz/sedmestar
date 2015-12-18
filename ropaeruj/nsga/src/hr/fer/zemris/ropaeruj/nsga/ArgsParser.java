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
    private final boolean mUseDecisionSpaceDensity;

    public ArgsParser(String[] args) {
        if (args.length !=6) {
            System.err.println("Parameters: problem_idx pop_size {decision-space|objective-space} maxiter share_sigma epsilon");
            System.exit(1);
        }

        mProblemIdx = Integer.valueOf(args[0]);

        mPopulationCount = Integer.valueOf(args[1]);

        mUseDecisionSpaceDensity = args[2].equals("decision-space");

        mMaxIterCount = Integer.valueOf(args[3]);

        mShareSigma = Double.valueOf(args[4]);

        mEpsilon = Double.valueOf(args[5]);

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

    public boolean useDecisionSpaceDensity() {
        return mUseDecisionSpaceDensity;
    }

    public int getProblemIdx() {
        return mProblemIdx;
    }
}
