package hr.fer.zemris.apr.ga;

import java.util.Random;

/**
 * Created by ivan on 1/3/16.
 */
public class GreyBinaryDecoderA extends GreyBinaryDecoder {
    public GreyBinaryDecoderA(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public BitvectorSolution crossoverAndMutate(BitvectorSolution mama, BitvectorSolution papa, Random random, double... mutationParams) {
        BitvectorSolution child = mama.newLikeThis();
        for (int i = 0; i < child.mBytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                child.mBytes[i] |=(random.nextBoolean()?mama.mBytes[i]:papa.mBytes[i]) & (1<<j);
                child.mBytes[i] ^=(random.nextDouble()<=mutationParams[0]?1:0)<<j;
            }
        }
        return child;
    }
}
