package hr.fer.zemris.apr.ga;

import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public final class BitvectorSolution extends SingleObjectiveSolution {
    public final byte[] mBytes;

    private BitvectorSolution(int byteCount) {
        mBytes = new byte[byteCount];
    }

    public static BitvectorSolution create(int bitCount) {
        return new BitvectorSolution((bitCount + 7) >> 3);
    }
    public BitvectorSolution newLikeThis() {
        return new BitvectorSolution(mBytes.length);
    }

    public BitvectorSolution randomize(Random random) {
        random.nextBytes(mBytes);
        return this;
    }
}
