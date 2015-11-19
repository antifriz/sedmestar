package hr.fer.zemris.ropaeruj.evo;


import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by ivan on 10/31/15.
 * <p>
 * preporucam set parametara: 02-zad-prijenosna.txt 100 0 10000 tournament:10 1
 * za dovoljno veliki max_iter_count skoro svaki put ce naci optimum uz err == 0
 */
public class GeneticAlgorithm {

    public static final int DIM = 5;
    private static final double LOWER_LIMITS = -4;
    private static final double UPPER_LIMITS = 4;
    public static final double MAX_EXP = Math.pow(10, 50);


    public static void main(String[] args) {

        ArgsParser argsParser = new ArgsParser(args);

        FileParser fileParser = new FileParser(argsParser.getFileName());
        IFunction function = arr -> {
            double sum = 0;
            for (int i = 0; i < fileParser.valueVector.length; i++) {
                double[] xes = fileParser.systemMatrix[i];
                double diff = (Math.sin(arr[0] + arr[1] * xes[0]) + arr[2] * Math.cos(xes[0] * (arr[3] + xes[1])) / (1 + Math.exp(Math.pow(xes[0] - arr[4], 2)))) - fileParser.valueVector[i];
                sum += diff * diff;
            }
            return sum;
        };

        Random random = new Random();

        double[] lower = new double[DIM];
        Arrays.fill(lower, LOWER_LIMITS);
        double[] upper = new double[DIM];
        Arrays.fill(upper, UPPER_LIMITS);

        Chromosome[] currentPopulation = new Chromosome[argsParser.getPopulationCount()];
        for (int i = 0; i < currentPopulation.length; i++) {
            currentPopulation[i] = new Chromosome(DIM);
            currentPopulation[i].randomize(random, lower, upper);
        }
        Chromosome[] nextPopulation = new Chromosome[currentPopulation.length];
        evaluate(function, currentPopulation);

        geneticAlgorithmLoop(argsParser, function, random, currentPopulation, nextPopulation);

    }

    private static void geneticAlgorithmLoop(ArgsParser argsParser, IFunction function, Random random, Chromosome[] currentPopulation, Chromosome[] nextPopulation) {
        int iterCount = 0;
        while (true) {
            if (argsParser.isRouletteWheel()) {

                elitism(currentPopulation);

                nextPopulation[0] = currentPopulation[0];
            }

            print(currentPopulation, iterCount);

            iterCount++;

            if (isFinished(iterCount, currentPopulation, argsParser)) {
                System.out.printf("Ended with %d iterations, error %f -> %s\n", iterCount - 1, currentPopulation[0].fitness, currentPopulation[0]);
                return;
            }

            if (argsParser.isRouletteWheel()) {
                nextPopulationUsingRouletteWheelSelection(iterCount, argsParser, random, currentPopulation, nextPopulation);
            } else {
                nextPopulationUsingKTournament(iterCount, argsParser, random, currentPopulation, nextPopulation);
            }

            Chromosome[] k = currentPopulation;
            currentPopulation = nextPopulation;
            nextPopulation = k;

            evaluate(function, currentPopulation);
        }
    }

    private static void nextPopulationUsingRouletteWheelSelection(int iterCount, ArgsParser argsParser, Random random, Chromosome[] currentPopulation, Chromosome[] nextPopulation) {
        for (int i = 1; i < nextPopulation.length; i++) {
            Chromosome mama = rouletteWheelSelection(currentPopulation, random);
            Chromosome papa = rouletteWheelSelection(currentPopulation, random);

            tweakChild(iterCount, argsParser, random, nextPopulation, i, mama, papa);
        }
    }

    private static void tweakChild(int iterCount, ArgsParser argsParser, Random random, Chromosome[] nextPopulation, int i, Chromosome mama, Chromosome papa) {
        Chromosome child = mama.newLikeThis();
        for (int j = 0; j < child.values.length; j++) {
            double min = Math.min(mama.values[j], papa.values[j]);
            double max = Math.max(mama.values[j], papa.values[j]);
            child.values[j] = min + (max - min) * random.nextDouble() + random.nextGaussian() * argsParser.getSigma() * Math.pow(1 - iterCount / (double) argsParser.getMaxIterCount(), 10);
        }
        nextPopulation[i] = child;
    }

    private static void nextPopulationUsingKTournament(int iterCount, ArgsParser argsParser, Random random, Chromosome[] currentPopulation, Chromosome[] nextPopulation) {
        Chromosome[] candidates = new Chromosome[argsParser.getTournamentN()];
        for (int j = 0; j < argsParser.getTournamentN(); j++) {
            candidates[j] = currentPopulation[random.nextInt(currentPopulation.length)];
        }
        Arrays.sort(candidates, Collections.<Chromosome>reverseOrder());

        Chromosome mama = candidates[0];
        Chromosome papa = candidates[1];

        Chromosome badOne = candidates[candidates.length - 1];


        int i = 0;
        for (; i < currentPopulation.length; i++) {
            if (currentPopulation[i] == badOne) {
                break;
            }
        }

        tweakChild(iterCount, argsParser, random, currentPopulation, i, mama, papa);
        System.arraycopy(currentPopulation, 0, nextPopulation, 0, currentPopulation.length);
    }

    private static boolean isFinished(int iterCount, Chromosome[] currentPopulation, ArgsParser argsParser) {
        return iterCount > argsParser.getMaxIterCount() || -currentPopulation[0].fitness < argsParser.getDesiredError();
    }

    private static void print(Chromosome[] currentPopulation, int iterCount) {
        System.out.printf("[%6d]", iterCount);
        System.out.printf("%s %f ", currentPopulation[0], currentPopulation[0].fitness);
        System.out.println();
    }

    private static void elitism(Chromosome[] currentPopulation) {
        double maxFitness = currentPopulation[0].fitness;
        int maxFitnessIndex = 0;
        for (int i = 1; i < currentPopulation.length; i++) {
            if (maxFitness < currentPopulation[i].fitness) {
                maxFitness = currentPopulation[i].fitness;
                maxFitnessIndex = i;
            }
        }
        Chromosome s = currentPopulation[0];
        currentPopulation[0] = currentPopulation[maxFitnessIndex];
        currentPopulation[maxFitnessIndex] = s;
    }

    private static double myexp(double v) {
        return Math.min(Math.exp(v), MAX_EXP);
    }

    private static void evaluate(IFunction function, Chromosome[] population) {
        Arrays.stream(population).parallel().forEach(solution -> solution.fitness = -function.valueAt(solution.values));
    }

    private static Chromosome rouletteWheelSelection(Chromosome[] array, Random random) {
        double minimal = Double.MAX_VALUE;
        double sum = 0;
        for (Chromosome anArray : array) {
            sum += anArray.fitness;
            minimal = Math.min(minimal, anArray.fitness);
        }
        sum -= minimal * array.length;

        double roulletePick = random.nextDouble() * sum;

        double it = 0;
        for (Chromosome a : array) {
            it += a.fitness - minimal;
            if (it > roulletePick) {
                return a;
            }
        }
        return array[random.nextInt(array.length)];
    }

}
