package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.*;

import java.util.Arrays;
import java.util.Random;

public class RegresijaSustava {

    public static final int DIM = 6;
    public static final double COUNT = 5;
    private static final double LOWER_LIMITS = -10;
    private static final double UPPER_LIMITS = 10;


    public static void main(String[] args) {

        int initial = 1000;
        int outer = 100;
        int inner = 100;
        double deltas = 1;
        double alpha = getAlpha(initial, outer);

        Config config = new Config(deltas, alpha, initial, outer, inner);

        ArgsParser argsParser = new ArgsParser(args);

        FileParser fileParser = new FileParser(argsParser.getFileName());
        IFunction function = arr -> {
            double sum = 0;
            for (int i = 0; i < fileParser.valueVector.length; i++) {
                double[] xes = fileParser.systemMatrix[i];
                double diff = (arr[0] * xes[0] + arr[1] * xes[1] + arr[2] * Math.exp(arr[3] * xes[2]) * (1 + Math.cos(arr[4] * xes[3])) + arr[5] * xes[4]) - fileParser.valueVector[i];
                sum += diff * diff;
            }
            return sum;
        };

        Random random = new Random();

        ITempSchedule schedule = new GeometricTempSchedule(config.getAlpha(), config.getInitial(), config.getInner(), config.getOuter());


        double[] lowerLimits = new double[RegresijaSustava.DIM];
        Arrays.fill(lowerLimits, LOWER_LIMITS);

        double[] upperLimits = new double[RegresijaSustava.DIM];
        Arrays.fill(upperLimits, UPPER_LIMITS);

        lowerLimits[4] = 0;
        upperLimits[4] = Math.PI;

        if (argsParser.isDecimal()) {
            IDecoder decoder = new PassThroughDecoder();
            DoubleArraySolution solution = new DoubleArraySolution(RegresijaSustava.DIM);

            solution.randomize(random, lowerLimits, upperLimits);

            double deltasArray[] = new double[6];
            deltasArray[4] *= 10;
            Arrays.fill(deltasArray, config.getDeltas());
            INeighborhood neighborhood = new DoubleArrayNormNeighborhood(deltasArray, random);

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(
                    decoder,
                    neighborhood,
                    solution,
                    function,
                    schedule,
                    true
            );
            simulatedAnnealing.mWillLog = true;
            simulatedAnnealing.run();
            System.out.printf("END: %s @ %f\n", simulatedAnnealing.getBest(), Math.sqrt(simulatedAnnealing.getBest().value));
        } else {
            int[] bits = new int[RegresijaSustava.DIM];
            Arrays.fill(bits, argsParser.getBitsPerVariable());

            IDecoder decoder = new GreyBinaryDecoder(lowerLimits, upperLimits, bits, RegresijaSustava.DIM);
            int bitCount = RegresijaSustava.DIM * argsParser.getBitsPerVariable();
            BitvectorSolution solution = BitvectorSolution.create(bitCount);
            INeighborhood neighborhood = new BitvectorNeighborhood(random, bitCount);

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(
                    decoder,
                    neighborhood,
                    solution,
                    function,
                    schedule,
                    true
            );
            simulatedAnnealing.mWillLog = true;
            simulatedAnnealing.run();
            System.out.printf("END: %s @ %f\n", decoder.toString(simulatedAnnealing.getBest()), Math.sqrt(simulatedAnnealing.getBest().value));
        }
    }

    private static double getAlpha(double initial, double outer) {
        return Math.pow(0.1 / initial, 1 / outer);
    }
}
