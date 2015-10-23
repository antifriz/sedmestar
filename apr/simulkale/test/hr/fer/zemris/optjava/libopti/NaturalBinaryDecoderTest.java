package hr.fer.zemris.optjava.libopti;

import hr.fer.zemris.optjava.dz3.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 10/23/15.
 */
public class NaturalBinaryDecoderTest {
    private Random mRandom;

    @Before
    public void setUp() throws Exception {
        mRandom = new Random(69);

    }

    @Test
    public void testBits() throws Exception {
        IDecoder passThroughDecoder = new PassThroughDecoder();


        int n = 2;
        DoubleArraySolution solution = new DoubleArraySolution(n);

        double[] lowerLimits = new double[n];
        double[] upperLimits = new double[n];
        Arrays.fill(upperLimits, 1);
        solution.randomize(mRandom, lowerLimits, upperLimits);
        double[] deltas = new double[n];
        Arrays.fill(deltas, 0.01);
        INeighborhood neighborhood = new DoubleArrayNormNeighborhood(deltas);

        IFunction function = arr -> Math.pow(arr[0], 2) + Math.pow(arr[1], 2);

        int tInnerLimit = 1;
        double alpha = 0.999995;
        int tOuterLimit = 100000000;
        ITempSchedule schedule = new GeometricTempSchedule(alpha, 1, tInnerLimit, tOuterLimit);
        System.out.printf("%f %f %d %d\n", alpha, 1.0f, tInnerLimit, tOuterLimit);

        ISimulatedAnnealingListener listener = new VerboseSimulatedAnnealingListener<>();
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(passThroughDecoder, neighborhood, solution, function, schedule, true, listener);
        simulatedAnnealing.run();
        System.out.println(simulatedAnnealing.getCurrentBest().value);

    }

}
