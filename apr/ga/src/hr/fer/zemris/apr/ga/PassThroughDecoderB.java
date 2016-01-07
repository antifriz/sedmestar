package hr.fer.zemris.apr.ga;

import java.util.Random;

/**
 * Created by ivan on 1/3/16.
 */
public class PassThroughDecoderB extends PassThroughDecoder {
    @Override
    public DoubleArraySolution crossoverAndMutate(DoubleArraySolution mama, DoubleArraySolution papa, Random random, double ... mutationParams) {
        DoubleArraySolution child = mama.newLikeThis();
        for (int j = 0; j < child.values.length; j++) {
            child.values[j] = random.nextBoolean()?mama.values[j]:papa.values[j] + random.nextGaussian() * mutationParams[0];
        }
        return child;
    }
}
