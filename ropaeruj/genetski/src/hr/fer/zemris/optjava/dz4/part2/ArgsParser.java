package hr.fer.zemris.optjava.dz4.part2;


/**
 * Created by ivan on 10/23/15.
 */
public class ArgsParser {
    private final String mFileName;
    private final Integer mPopulationCount;
    private final Integer mSatisfiableContainerSize;
    private final Integer mMaxIterCount;
    private final Integer mN;
    private final Integer mM;
    private final Boolean mP;

    public ArgsParser(String[] args) {
        boolean isOk = true;
        if (args.length != 6) {
            isOk = false;
        }
        mFileName = args[0];

        mPopulationCount = Integer.valueOf(args[1]);


        mN = Integer.valueOf(args[2]);
        mM = Integer.valueOf(args[3]);

        if (mN < 2 || mM < 2) {
            isOk = false;
        }

        mP = Boolean.valueOf(args[4]);

        mMaxIterCount = Integer.valueOf(args[5]);

        mSatisfiableContainerSize = Integer.valueOf(args[6]);

        if (!isOk) {
            System.err.println("Parameters: path_to_data population_count desired_error max_iter_count (rouletteWheel|tournament:n) sigma");
            System.exit(1);
        }
    }

    public String getFileName() {
        return mFileName;
    }


    public Integer getPopulationCount() {
        return mPopulationCount;
    }

    public Integer getSatisfiableContainerSize() {
        return mSatisfiableContainerSize;
    }

    public Integer getMaxIterCount() {
        return mMaxIterCount;
    }

    public Integer getN() {
        return mN;
    }

    public Integer getM() {
        return mM;
    }

    public Boolean getP() {
        return mP;
    }
}
