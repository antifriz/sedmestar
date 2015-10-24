package hr.fer.zemris.optjava.libopti;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 10/22/15.
 */
public final class BitvectorSolution extends SingleObjectiveSolution {
    public byte[] mBytes;

    public BitvectorSolution(int n) {
        mBytes = new byte[(n + Byte.SIZE - 1) / Byte.SIZE];
    }

    public BitvectorSolution newLikeThis() {
        return new BitvectorSolution(mBytes.length);
    }

    public BitvectorSolution duplicate() {
        BitvectorSolution bitvectorSolution = newLikeThis();
        System.arraycopy(mBytes, 0, bitvectorSolution.mBytes, 0, mBytes.length);
        return bitvectorSolution;
    }

    public void randomize(Random random) {
        random.nextBytes(mBytes);
    }

    @Override
    public String toString() {
        return Arrays.toString(mBytes);
    }
}
