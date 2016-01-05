package hr.fer.zemris.ropaeruj.nsga2;


/**
 * Created by ivan on 10/23/15.
 */
final class ArgsParser {
    private final int mPopulationCount;
    private final int mMaxIterCount;
    private final int mProblemIdx;

    public ArgsParser(String[] args) {
        if (args.length !=3) {
            System.err.println("Parameters: problem_idx pop_size maxiter share_sigma epsilon");
            System.exit(1);
        }

        mProblemIdx = Integer.valueOf(args[0]);

        mPopulationCount = Integer.valueOf(args[1]);

        mMaxIterCount = Integer.valueOf(args[2]);
    }

    public Integer getPopulationCount() {
        return mPopulationCount;
    }

    public Integer getMaxIterCount() {
        return mMaxIterCount;
    }

    public int getProblemIdx() {
        return mProblemIdx;
    }
}
