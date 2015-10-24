package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 10/23/15.
 */
public class ArgsParser {
    public static final int LOWER_LIMITS = -1;
    public static final int UPPER_LIMITS = 1;
    private final String mFileName;
    private final IDecoder mDecoder;
    private final SingleObjectiveSolution mInitialSolution;
    private final Random mRand;

    public ArgsParser(String[] args, Random rand) {
        mRand = rand;
        if (args.length != 2) {
            System.err.printf("Parameters: algorithm path_to_data double|bits:n");
            System.exit(0);
        }
        mFileName = args[0];

        double[] lowerLimits = new double[RegresijaSustava.DIM];
        Arrays.fill(lowerLimits, LOWER_LIMITS);

        double[] upperLimits = new double[RegresijaSustava.DIM];
        Arrays.fill(upperLimits, UPPER_LIMITS);

        String type = args[1];
        if (type.equals("decimal")) {
            mDecoder = new PassThroughDecoder();
            DoubleArraySolution solution = new DoubleArraySolution(RegresijaSustava.DIM);
//            solution.values[0] = 7;
//            solution.values[1] =-3;
//            solution.values[2] = 2;
//            solution.values[3] = 1;
//            solution.values[4] = 3;
//            solution.values[5] = 3;

            lowerLimits[4] = 0;
            upperLimits[4] = Math.PI;
            solution.randomize(mRand, lowerLimits, upperLimits);
            mInitialSolution = solution;
        } else {
            String parts[] = type.split(":");
            int bitsPerVariable = Integer.valueOf(parts[1]);
            int[] bits = new int[RegresijaSustava.DIM];
            Arrays.fill(bits, bitsPerVariable);

            mDecoder = new GreyBinaryDecoder(lowerLimits, upperLimits, bits, RegresijaSustava.DIM);

            mInitialSolution = new BitvectorSolution(RegresijaSustava.DIM);
        }
    }

    public String getFileName() {
        return mFileName;
    }

    public IDecoder getDecoder() {
        return mDecoder;
    }


    public SingleObjectiveSolution getInitialSolution() {
        return mInitialSolution;
    }
}
