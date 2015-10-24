package hr.fer.zemris.optjava.dz3;

import hr.fer.zemris.optjava.libopti.DoubleArrayNormNeighborhood;
import hr.fer.zemris.optjava.libopti.IFunction;
import hr.fer.zemris.optjava.libopti.INeighborhood;

import java.util.Arrays;
import java.util.Random;

public class RegresijaSustava {

    public static final int DIM = 6;
    public static final double COUNT = 5;


    public static void main(String[] args) {

        ArgsParser argsParser = new ArgsParser(args, new Random());

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

        int initial = 1000;
        int outer = 1000;
        int inner = 1000;
        double deltas = 1;
        double alpha = getAlpha(initial, outer);
        runAlgorithm(new Config(deltas, alpha, initial, outer, inner), argsParser, function);
    }

    private static double getAlpha(double initial, double outer) {
        return Math.pow(0.1 / initial, 1 / outer);
    }

    private static double runAlgorithm(Config config, ArgsParser argsParser, IFunction function) {
        double deltas[] = new double[DIM];
        deltas[4] *= 10;
        Arrays.fill(deltas, config.getDeltas());

        INeighborhood neighborhood = new DoubleArrayNormNeighborhood(deltas);

        ITempSchedule schedule = new GeometricTempSchedule(config.getAlpha(), config.getInitial(), config.getInner(), config.getOuter());
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(argsParser.getDecoder(), neighborhood, argsParser.getInitialSolution(), function, schedule, true);
        simulatedAnnealing.mWillLog = true;
        simulatedAnnealing.run();
        System.out.printf("END: %s @ %f\n", simulatedAnnealing.getBest(), Math.sqrt(simulatedAnnealing.getBest().value));

        return simulatedAnnealing.getBest().value;
    }
}
