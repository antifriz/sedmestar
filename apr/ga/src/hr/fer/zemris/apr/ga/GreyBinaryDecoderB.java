package hr.fer.zemris.apr.ga;

import java.util.Random;

/**
 * Created by ivan on 1/3/16.
 */
public class GreyBinaryDecoderB extends GreyBinaryDecoder {
    public GreyBinaryDecoderB(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public BitvectorSolution crossoverAndMutate(BitvectorSolution mama, BitvectorSolution papa, Random random, double ...mutationParams) {
        BitvectorSolution child = mama.newLikeThis();
        int limit = random.nextInt(mTotalBits);
        boolean firstPapa = random.nextBoolean();
        for (int i = 0; i < child.mBytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                child.mBytes[i] |=((i*8+j<limit && firstPapa) || (i*8+j>=limit && !firstPapa)?mama.mBytes[i]:papa.mBytes[i]) & (1<<j);
                child.mBytes[i] ^=(random.nextInt(2))<<j;
            }
        }
        return child;
    }
}
