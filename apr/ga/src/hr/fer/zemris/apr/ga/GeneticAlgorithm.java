package hr.fer.zemris.apr.ga;


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

    private static final double LOWER_LIMITS = -50;
    private static final double UPPER_LIMITS = 150;
    public static final double MAX_EXP = Math.pow(10, 50);


    public static void main(String[] args) {
        run(new ArgsHolder());
    }

    public static double run(ArgsHolder argsHolder) {
        Random random = new Random();

        AbstractFunctionToOptimize function = Functions.get(argsHolder.functionIdx);

        int dim = function.dimension(argsHolder.desiredDim);
        double[] lower = new double[dim];
        Arrays.fill(lower, LOWER_LIMITS);
        double[] upper = new double[dim];
        Arrays.fill(upper, UPPER_LIMITS);


        SingleObjectiveSolution[] currentPopulation = new SingleObjectiveSolution[argsHolder.popCnt];
        for (int i = 0; i < currentPopulation.length; i++) {
            currentPopulation[i] = argsHolder.isBinary ? BitvectorSolution.create(argsHolder.bitCount * argsHolder.desiredDim).randomize(random) : new DoubleArraySolution(argsHolder.desiredDim).randomize(random, lower, upper);
        }
        SingleObjectiveSolution[] nextPopulation = new SingleObjectiveSolution[currentPopulation.length];

        double result;
        if (argsHolder.isBinary) {
            int[] bits = new int[argsHolder.desiredDim];
            Arrays.fill(bits, argsHolder.bitCount);
            result = geneticAlgorithmLoop(argsHolder, function, random, currentPopulation, nextPopulation, new GreyBinaryDecoderA(lower, upper, bits, argsHolder.desiredDim));

        } else {
            result = geneticAlgorithmLoop(argsHolder, function, random, currentPopulation, nextPopulation, new PassThroughDecoderA());
        }

        return result;

    }

    private static <Chromosome extends SingleObjectiveSolution> double geneticAlgorithmLoop(ArgsHolder argsHolder, AbstractFunctionToOptimize function, Random random, SingleObjectiveSolution[] currentPopulation, SingleObjectiveSolution[] nextPopulation, IDecoder<Chromosome> decoder) {

        evaluate(function, currentPopulation, decoder);

        SingleObjectiveSolution last = null;

        int iterCount = 0;
        while (true) {
            elitism(currentPopulation);
            nextPopulation[0] = currentPopulation[0];

            if(currentPopulation[0] != last){
                last = currentPopulation[0];
                if(-currentPopulation[0].fitness<1 && argsHolder.verbose){
                    print(currentPopulation, iterCount, decoder);
                }
            }

            iterCount++;

            if (isFinished(iterCount, currentPopulation, argsHolder)) {
                if(argsHolder.verbose) System.out.printf("Ended with %d iterations, error %f -> %s\n", iterCount - 1, -currentPopulation[0].fitness, decoder.toString((Chromosome) currentPopulation[0]));
                return -currentPopulation[0].fitness;
            }

            if (argsHolder.isRoulette) {
                nextPopulationUsingRouletteWheelSelection(iterCount, argsHolder, random, currentPopulation, nextPopulation, decoder);
            } else {
                nextPopulationUsingKTournament(iterCount, argsHolder, random, currentPopulation, nextPopulation, decoder);
            }

            SingleObjectiveSolution[] k = currentPopulation;
            currentPopulation = nextPopulation;
            nextPopulation = k;

            evaluate(function, currentPopulation, decoder);
        }
    }

    private static <Chromosome extends SingleObjectiveSolution> void nextPopulationUsingRouletteWheelSelection(int iterCount, ArgsHolder argsHolder, Random random, SingleObjectiveSolution[] currentPopulation, SingleObjectiveSolution[] nextPopulation, IDecoder<Chromosome> decoder) {
        for (int i = 1; i < nextPopulation.length; i++) {
            SingleObjectiveSolution mama = rouletteWheelSelection(currentPopulation, random);
            SingleObjectiveSolution papa = rouletteWheelSelection(currentPopulation, random);

            SingleObjectiveSolution child = decoder.crossoverAndMutate((Chromosome) mama, (Chromosome) papa, random, argsHolder.mutationProba * Math.pow(1 - iterCount / (double) argsHolder.maxIter, 10));

            nextPopulation[i] = child;
        }
    }

    private static <Chromosome extends SingleObjectiveSolution> void nextPopulationUsingKTournament(int iterCount, ArgsHolder argsHolder, Random random, SingleObjectiveSolution[] currentPopulation, SingleObjectiveSolution[] nextPopulation, IDecoder<Chromosome> decoder) {
        for (int i = 1; i < nextPopulation.length; i++) {
            SingleObjectiveSolution[] candidates = new SingleObjectiveSolution[argsHolder.tournamentCnt];
            for (int j = 0; j < argsHolder.tournamentCnt; j++) {
                candidates[j] = currentPopulation[random.nextInt(currentPopulation.length)];
            }
            Arrays.sort(candidates, Collections.<SingleObjectiveSolution>reverseOrder());

            SingleObjectiveSolution mama = candidates[0];
            SingleObjectiveSolution papa = candidates[1];

            SingleObjectiveSolution child = decoder.crossoverAndMutate((Chromosome) mama, (Chromosome) papa, random, argsHolder.mutationProba /* * Math.pow(1 - iterCount / (double) argsHolder.maxIter,10)*/);

            nextPopulation[i] = child;
        }
    }

    private static boolean isFinished(int iterCount, SingleObjectiveSolution[] currentPopulation, ArgsHolder argsHolder) {
        return iterCount > argsHolder.maxIter || -currentPopulation[0].fitness < argsHolder.desiredError;
    }

    private static <Chromosome extends SingleObjectiveSolution> void print(SingleObjectiveSolution[] currentPopulation, int iterCount, IDecoder<Chromosome> decoder) {
        System.out.printf("[%6d]", iterCount);
        System.out.printf("%s %f ", decoder.toString((Chromosome) currentPopulation[0]), -currentPopulation[0].fitness);
        System.out.println();
    }

    private static void elitism(SingleObjectiveSolution[] currentPopulation) {
        double maxFitness = currentPopulation[0].fitness;
        int maxFitnessIndex = 0;
        for (int i = 1; i < currentPopulation.length; i++) {
            if (maxFitness < currentPopulation[i].fitness) {
                maxFitness = currentPopulation[i].fitness;
                maxFitnessIndex = i;
            }
        }
        SingleObjectiveSolution s = currentPopulation[0];
        currentPopulation[0] = currentPopulation[maxFitnessIndex];
        currentPopulation[maxFitnessIndex] = s;
    }

    private static double myexp(double v) {
        return Math.min(Math.exp(v), MAX_EXP);
    }

    private static <Chromosome extends SingleObjectiveSolution> void evaluate(AbstractFunctionToOptimize function, SingleObjectiveSolution[] population, IDecoder<Chromosome> decoder) {
        Arrays.stream(population).parallel().forEach(solution -> solution.fitness = -function.valueAt(new Point(decoder.decode((Chromosome) solution))));
    }

    private static <Chromosome extends SingleObjectiveSolution> Chromosome rouletteWheelSelection(Chromosome[] array, Random random) {
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
