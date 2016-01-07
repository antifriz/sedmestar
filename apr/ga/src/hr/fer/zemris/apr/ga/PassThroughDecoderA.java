package hr.fer.zemris.apr.ga;

import java.util.Random;

/**
 * Created by ivan on 1/3/16.
 */
public class PassThroughDecoderA extends PassThroughDecoder {

    @Override
    public DoubleArraySolution crossoverAndMutate(DoubleArraySolution mama, DoubleArraySolution papa, Random random, double... mutationParams) {
        DoubleArraySolution child = mama.newLikeThis();
        for (int j = 0; j < child.values.length; j++) {
            double min = Math.min(mama.values[j], papa.values[j]);
            double max = Math.max(mama.values[j], papa.values[j]);
            child.values[j] = min + (max - min) * random.nextDouble() + (random.nextDouble()<=mutationParams[0]?random.nextGaussian() * mutationParams[1]:0);
        }
        return child;
    }
}
