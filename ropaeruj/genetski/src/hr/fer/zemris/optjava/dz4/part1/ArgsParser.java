package hr.fer.zemris.optjava.dz4.part1;


/**
 * Created by ivan on 10/23/15.
 */
public class ArgsParser {
    private final String mFileName;
    private final int mTournamentN;
    private final boolean mIsRouletteWheel;
    private final Integer mPopulationCount;
    private final Double mDesiredError;
    private final Integer mMaxIterCount;
    private final Double mSigma;

    public ArgsParser(String[] args) {
        if (args.length != 6) {
            System.err.printf("Parameters: path_to_data population_count desired_error max_iter_count (rouletteWheel|tournament:n) sigma");
            System.exit(0);
        }
        mFileName = args[0];

        mPopulationCount = Integer.valueOf(args[1]);

        mDesiredError = Double.valueOf(args[2]);

        mMaxIterCount = Integer.valueOf(args[3]);


        String type = args[4];
        if (type.equals("rouletteWheel")) {
            mIsRouletteWheel = true;
            mTournamentN = -1;
        } else {
            mIsRouletteWheel = false;
            String parts[] = type.split(":");
            mTournamentN = Integer.valueOf(parts[1]);
        }

        mSigma = Double.valueOf(args[5]);
    }

    public String getFileName() {
        return mFileName;
    }

    public int getTournamentN() {
        return mTournamentN;
    }

    public boolean isRouletteWheel() {
        return mIsRouletteWheel;
    }

    public Integer getPopulationCount() {
        return mPopulationCount;
    }

    public Double getDesiredError() {
        return mDesiredError;
    }

    public Integer getMaxIterCount() {
        return mMaxIterCount;
    }

    public Double getSigma() {
        return mSigma;
    }
}
