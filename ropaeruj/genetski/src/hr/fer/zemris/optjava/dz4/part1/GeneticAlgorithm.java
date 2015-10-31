package hr.fer.zemris.optjava.dz4.part1;


import hr.fer.zemris.optjava.libopti.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 */
public class GeneticAlgorithm {

    public static final int DIM = 6;
    private static final double LOWER_LIMITS = -10;
    private static final double UPPER_LIMITS = 10;
    public static final double MAX_EXP = Math.pow(10, 50);


    public static void main(String[] args) {

        ArgsParser argsParser = new ArgsParser(args);

        FileParser fileParser = new FileParser(argsParser.getFileName());
        IFunction function = arr -> {
            double sum = 0;
            for (int i = 0; i < fileParser.valueVector.length; i++) {
                double[] xes = fileParser.systemMatrix[i];
                double diff = (arr[0] * xes[0] + arr[1] * xes[1] + arr[2] * myexp(arr[3] * xes[2]) * (1 + Math.cos(arr[4] * xes[3])) + arr[5] * xes[4]) - fileParser.valueVector[i];
                sum += diff * diff;
            }
            return sum;
        };

        Random random = new Random();

        double[] lower = new double[DIM];
        Arrays.fill(lower, LOWER_LIMITS);
        double[] upper = new double[DIM];
        Arrays.fill(upper, UPPER_LIMITS);

        ISolutionFactory<DoubleArraySolution> solutionFactory = new DoubleArraySolutionFactory(DIM, random, lower, upper);

        geneticAlgorithm(argsParser, function, random, solutionFactory);

    }

    private static double[] geneticAlgorithm(ArgsParser argsParser, IFunction function, Random random, ISolutionFactory<DoubleArraySolution> solutionFactory) {
        IDecoder<DoubleArraySolution> decoder = new PassThroughDecoder();

        DoubleArraySolution[] currentPopulation = solutionFactory.newRandomizedArray(argsParser.getPopulationCount());
        DoubleArraySolution[] nextPopulation = new DoubleArraySolution[currentPopulation.length];
        evaluate(function, decoder, currentPopulation);

        return geneticAlgorithmLoop(argsParser, function, random, decoder, currentPopulation, nextPopulation);
    }

    private static double[] geneticAlgorithmLoop(ArgsParser argsParser, IFunction function, Random random, IDecoder<DoubleArraySolution> decoder, DoubleArraySolution[] currentPopulation, DoubleArraySolution[] nextPopulation) {
        int iterCount = 0;
        while (true) {
            elitism(currentPopulation);
            nextPopulation[0] = currentPopulation[0];

            print(decoder, currentPopulation, iterCount);

            iterCount++;

            if (isFinished(iterCount, currentPopulation, argsParser)) {
                System.out.printf("Ended with %d iterations, error %f -> %s\n",iterCount-1,currentPopulation[0].value,decoder.toString(currentPopulation[0]));
                return decoder.decode(currentPopulation[0]);
            }

            if (argsParser.isRouletteWheel()) {
                nextPopulationUsingRouletteWheelSelection(iterCount, argsParser, random, currentPopulation, nextPopulation);
            } else {
                nextPopulationUsingKTournament(iterCount, argsParser, random, currentPopulation, nextPopulation);
            }

            DoubleArraySolution[] k = currentPopulation;
            currentPopulation = nextPopulation;
            nextPopulation = k;

            evaluate(function, decoder, currentPopulation);
        }
    }

    private static void nextPopulationUsingRouletteWheelSelection(int iterCount, ArgsParser argsParser, Random random, DoubleArraySolution[] currentPopulation, DoubleArraySolution[] nextPopulation) {
        for (int i = 1; i < nextPopulation.length; i++) {
            DoubleArraySolution mama = rouletteWheelSelection(currentPopulation, random);
            DoubleArraySolution papa = rouletteWheelSelection(currentPopulation, random);

            tweakChild(iterCount, argsParser, random, nextPopulation, i, mama, papa);
        }
    }

    private static void tweakChild(int iterCount, ArgsParser argsParser, Random random, DoubleArraySolution[] nextPopulation, int i, DoubleArraySolution mama, DoubleArraySolution papa) {
        DoubleArraySolution child = mama.newLikeThis();
        for (int j = 0; j < child.values.length; j++) {
            double min = Math.min(mama.values[j], papa.values[j]);
            double max = Math.max(mama.values[j], papa.values[j]);
            child.values[j] = min + (max - min) * random.nextDouble() + random.nextGaussian() * argsParser.getSigma() * Math.pow(1-iterCount/(double)argsParser.getMaxIterCount(),10);
        }
        nextPopulation[i] = child;
    }

    private static void nextPopulationUsingKTournament(int iterCount, ArgsParser argsParser, Random random, DoubleArraySolution[] currentPopulation, DoubleArraySolution[] nextPopulation) {
        for (int i = 1; i < nextPopulation.length; i++) {
            DoubleArraySolution[] candidates = new DoubleArraySolution[argsParser.getTournamentN()];
            for (int j = 0; j < argsParser.getTournamentN(); j++) {
                candidates[j] = currentPopulation[random.nextInt(currentPopulation.length)];
            }
            Arrays.sort(candidates, Collections.<DoubleArraySolution>reverseOrder());

            DoubleArraySolution mama = candidates[0];
            DoubleArraySolution papa = candidates[1];

            tweakChild(iterCount, argsParser, random, nextPopulation, i, mama, papa);
        }
    }

    private static boolean isFinished(int iterCount, DoubleArraySolution[] currentPopulation, ArgsParser argsParser) {
        return iterCount > argsParser.getMaxIterCount() || Math.sqrt(currentPopulation[0].value) < argsParser.getDesiredError();
    }

    private static void print(IDecoder<DoubleArraySolution> decoder, DoubleArraySolution[] currentPopulation, int iterCount) {
        if (iterCount % 100 == 0) {
            System.out.printf("[%6d]", iterCount);
            for (DoubleArraySolution aCurrentPopulation : currentPopulation) {
                System.out.printf("%s %f ", decoder.toString(aCurrentPopulation), Math.sqrt(aCurrentPopulation.value));
                break;
            }
            System.out.println();
        }
    }

    private static void elitism(DoubleArraySolution[] currentPopulation) {
        double maxFitness = currentPopulation[0].fitness;
        int maxFitnessIndex = 0;
        for (int i = 1; i < currentPopulation.length; i++) {
            if (maxFitness < currentPopulation[i].fitness) {
                maxFitness = currentPopulation[i].fitness;
                maxFitnessIndex = i;
            }
        }
        DoubleArraySolution s = currentPopulation[0];
        currentPopulation[0] = currentPopulation[maxFitnessIndex];
        currentPopulation[maxFitnessIndex] = s;
    }

    private static double myexp(double v) {
        return Math.min(Math.exp(v), MAX_EXP);
    }

    private static void evaluate(IFunction function, IDecoder<DoubleArraySolution> decoder, DoubleArraySolution[] population) {
        Arrays.stream(population).parallel().forEach(x -> evaluate(x, function, decoder));
    }


    private static void evaluate(DoubleArraySolution solution, IFunction function, IDecoder<DoubleArraySolution> decoder) {
        solution.value = function.valueAt(decoder.decode(solution));
        solution.fitness = -solution.value;
    }

    private static DoubleArraySolution rouletteWheelSelection(DoubleArraySolution[] array, Random random) {
        double minimal = Double.MAX_VALUE;
        double sum = 0;
        for (DoubleArraySolution anArray : array) {
            sum += anArray.fitness;
            minimal = Math.min(minimal, anArray.fitness);
        }
        sum -= minimal * array.length;

        double roulletePick = random.nextDouble() * sum;

        double it = 0;
        for (DoubleArraySolution a : array) {
            it += a.fitness - minimal;
            if (it > roulletePick) {
                return a;
            }
        }
        return array[random.nextInt(array.length)];
    }

}
