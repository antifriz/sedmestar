package hr.fer.zemris.optjava.dz3;

/**
 * Created by ivan on 10/23/15.
 */
public class ArgsParser {
    private final String mFileName;
    private final int mBitsPerVariable;
    private final boolean mIsDecimal;

    public ArgsParser(String[] args) {
        if (args.length != 2) {
            System.err.printf("Parameters: algorithm path_to_data double|bits:n");
            System.exit(0);
        }
        mFileName = args[0];

        String type = args[1];
        if (type.equals("decimal")) {
            mIsDecimal = true;
            mBitsPerVariable = -1;
        } else {
            mIsDecimal = false;
            String parts[] = type.split(":");
            mBitsPerVariable = Integer.valueOf(parts[1]);

        }
    }

    public String getFileName() {
        return mFileName;
    }

    public int getBitsPerVariable() {
        return mBitsPerVariable;
    }

    public boolean isDecimal() {
        return mIsDecimal;
    }
}
