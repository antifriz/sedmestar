package hr.fer.zemris.optjava.dz5.part2;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by ivan on 11/6/15.
 */
public class GeneticAlgorithm {

    public static final double SUCCESS_RATIO = 0.5;
    public static final int MAX_SELECTION_PRESSURE = 20;
    public static final int NUMBER_OF_ITERATIONS = 50;
    static final int K_TOURNAMENT_CONSTANT = 3;
    private static final int MAX_VILLAGE_COUNT = 16;
    public static final int POPULATION_COUNT = 400 * MAX_VILLAGE_COUNT;

    public static void main(String[] args) {
        run(args[0]);
    }

    static int run(String arg) {
        FileParser parser = new FileParser(arg);
        Random random = new Random();

        Function<int[], Double> fitnessFunction = permutation1 -> -OffspringSelection.cost(parser.distances, parser.prices, permutation1);

        PermutationChromosome[] initialPopulation = new PermutationChromosome[POPULATION_COUNT];
        Arrays.setAll(initialPopulation, i -> new PermutationChromosome(parser.dimension, random, fitnessFunction));

        for (int villageCount = MAX_VILLAGE_COUNT; villageCount > 0; villageCount /= 2) {

            int villagePopulation = (int) Math.ceil(POPULATION_COUNT / (double) villageCount);

            final PermutationChromosome[] finalInitialPopulation = initialPopulation;

            final double successRatio = SUCCESS_RATIO + (1 - SUCCESS_RATIO) * (1 - villageCount / (double) MAX_VILLAGE_COUNT);

            System.out.printf("Initializing %d villages, desired success ratio: %f\n", villageCount, successRatio);

            initialPopulation = IntStream.range(0, villageCount).parallel()
                    .mapToObj(index -> Arrays.copyOfRange(finalInitialPopulation, index * villagePopulation, Math.min((index + 1) * villagePopulation, finalInitialPopulation.length)))
                    .map(population -> OffspringSelection.run(population, fitnessFunction, ThreadLocalRandom.current(), population.length, successRatio, MAX_SELECTION_PRESSURE, NUMBER_OF_ITERATIONS))
                    .flatMap(Arrays::stream)
                    .toArray(PermutationChromosome[]::new);
            assert initialPopulation.length == POPULATION_COUNT;

            PermutationChromosome[] copy = Arrays.copyOf(initialPopulation, initialPopulation.length);

            Arrays.sort(copy, (a, b) -> Double.compare(b.fitness, a.fitness));
            System.out.printf("==========\nBEST: %d\n", (int) -copy[0].fitness);
        }
        Arrays.sort(initialPopulation, (a, b) -> Double.compare(b.fitness, a.fitness));
        System.out.println(Arrays.toString(initialPopulation));
        return (int) -initialPopulation[0].fitness;
    }


}
