package hr.fer.zemris.optjava.dz5.part2;

import java.util.*;
import java.util.function.Function;

/**
 * Created by ivan on 11/7/15.
 */
public class OffspringSelection {

    static PermutationChromosome[] run(PermutationChromosome[] initialPopulation, Function<int[], Double> fitnessFunction, Random random, int populationSize, double successRatio, double maxSelectionPressure, int numberOfIterations) {
        PermutationChromosome[] oldPopulation = Arrays.copyOf(initialPopulation, initialPopulation.length);

        int maxPoolSize = (int) Math.ceil(populationSize * maxSelectionPressure);
        int maxSuccessfulChildrenCount = (int) Math.ceil(populationSize * successRatio);

        PermutationChromosome[] poolArrayCache = new PermutationChromosome[maxPoolSize + 1];

        int i = 0;
        double actualSelectionPressure = 0;
        double comparisonFactor = 0;

        Set<PermutationChromosome> successfulChildren = new HashSet<>(maxSuccessfulChildrenCount + 1);
        ArrayDeque<PermutationChromosome> pool = new ArrayDeque<>(maxPoolSize + 1);

        while (i < numberOfIterations && (actualSelectionPressure < maxSelectionPressure)) {

            successfulChildren.clear();
            pool.clear();

            successfulChildren.add(Arrays.stream(oldPopulation).max((a, b) -> Double.compare(a.fitness, b.fitness)).get());

            updateChildren(fitnessFunction, random, oldPopulation, maxPoolSize, maxSuccessfulChildrenCount, comparisonFactor, successfulChildren, pool);

            actualSelectionPressure = (successfulChildren.size() + pool.size()) / (float) oldPopulation.length;

            int filledSize = pool.size() + successfulChildren.size();
            if (filledSize < oldPopulation.length) {
                for (int j = 0; j < oldPopulation.length - filledSize; j++) {
                    PermutationChromosome mama = pickMama(oldPopulation, random);
                    PermutationChromosome papa = pickPapa(oldPopulation, random);
                    PermutationChromosome child = produceChild(mama, papa, fitnessFunction, random);
                    pool.add(child);
                }
            }

            int populationIterator = 0;
            for (PermutationChromosome successfulChild : successfulChildren) {
                oldPopulation[populationIterator++] = successfulChild;
            }
            pool.toArray(poolArrayCache);
            System.arraycopy(poolArrayCache, 0, oldPopulation, populationIterator, oldPopulation.length - populationIterator);

            comparisonFactor = i / (double) numberOfIterations;
            i++;
        }

        int round = (int) Math.round(-Arrays.stream(oldPopulation).max((a, b) -> Double.compare(a.fitness, b.fitness)).get().fitness);
        System.out.printf("Best: %d Pressure @ termination: %4.1f\n",round, actualSelectionPressure);

        return oldPopulation;
    }

    private static void updateChildren(Function<int[], Double> fitnessFunction, Random random, PermutationChromosome[] oldPopulation, int maxPoolSize, int maxSuccessfulChildrenCount, double comparisonFactor, Set<PermutationChromosome> successfulChildren, ArrayDeque<PermutationChromosome> pool) {

        while (successfulChildren.size() < maxSuccessfulChildrenCount && (successfulChildren.size() + pool.size() < maxPoolSize)) {
            PermutationChromosome mama = pickMama(oldPopulation, random);
            PermutationChromosome papa = pickPapa(oldPopulation, random);
            PermutationChromosome child = produceChild(mama, papa, fitnessFunction, random);

            if (isSuccessfulChild(child, mama, papa, comparisonFactor)) {
                successfulChildren.add(child);
//                    System.out.println("Added child "+-mama.fitness + " " + -papa.fitness+" "+-child.fitness);
            } else {
                pool.add(child);
//                    System.out.println("Discarded child "+-mama.fitness + " " + -papa.fitness+" "+-child.fitness);
            }
        }
    }

    private static PermutationChromosome produceChild(PermutationChromosome mama, PermutationChromosome papa, Function<int[], Double> fitnessFunction, Random random) {
        int[] childGenes = crossover1(mama.values, papa.values, random);

        mutate(childGenes, random);

        return new PermutationChromosome(childGenes, fitnessFunction);
    }

    private static void mutate(int[] childGenes, Random random) {
        int length = childGenes.length;
        for (int i = 0; i < random.nextInt(2); i++) {
            int idx1 = random.nextInt(length);
            int idx2 = random.nextInt(length);

            int k = childGenes[idx1];
            childGenes[idx1] = childGenes[idx2];
            childGenes[idx2] = k;
        }
    }

    private static PermutationChromosome pickPapa(PermutationChromosome[] oldPopulation, Random random) {
        return kTournamentSelection(oldPopulation, random);
    }

    private static PermutationChromosome pickMama(PermutationChromosome[] oldPopulation, Random random) {
        return kTournamentSelection(oldPopulation, random);
    }

    private static PermutationChromosome kTournamentSelection(PermutationChromosome[] oldPopulation, Random random) {
        PermutationChromosome[] permutationChromosomes = new PermutationChromosome[GeneticAlgorithm.K_TOURNAMENT_CONSTANT];
        for (int i = 0; i < GeneticAlgorithm.K_TOURNAMENT_CONSTANT; i++) {
            permutationChromosomes[i] = pickUniform(oldPopulation, random);
        }
        double maximal = permutationChromosomes[0].fitness;
        int idx = 0;
        for (int i = 1; i < GeneticAlgorithm.K_TOURNAMENT_CONSTANT; i++) {
            if (maximal < permutationChromosomes[i].fitness) {
                idx = i;
                maximal = permutationChromosomes[i].fitness;
            }
        }
        return permutationChromosomes[idx];
    }

    private static PermutationChromosome pickUniform(PermutationChromosome[] population, Random random) {
        return population[random.nextInt(population.length)];
    }

    private static boolean isSuccessfulChild(PermutationChromosome child, PermutationChromosome mama, PermutationChromosome papa, double comparisonFactor) {
        double minimal = Math.min(mama.fitness, papa.fitness);
        double maximal = Math.max(mama.fitness, papa.fitness);

        return child.fitness > minimal + (maximal - minimal) * comparisonFactor;
    }

    private static int[] crossover1(int[] mama, int[] papa, Random random) {
        assert mama.length == papa.length;

        int length = mama.length;


        int rnd1 = random.nextInt(length);
        int rnd2 = random.nextInt(length);

        int from = Math.min(rnd1, rnd2);
        int to = Math.max(rnd1, rnd2);


        int[] crossoverSection = new int[length];
        Arrays.fill(crossoverSection, -1);
        System.arraycopy(mama, from, crossoverSection, from, to + 1 - from);

        int k = 0;
        outer:
        for (int i = 0; i < length; i++) {
            int candidate = papa[i];

            for (int j = from; j <= to; j++) {
                if (mama[j] == candidate) {
                    continue outer;
                }
            }
            if (k == from) {
                k = to + 1;
            }
            crossoverSection[k++] = candidate;
        }
        return crossoverSection;
    }

    public static double cost(double[][] price, double[][] distance, int[] permutation) {
        double sum = 0;
        int dim = price.length;
        for (int i = 0; i < dim; i++) {
            double[] prices = price[i];
            double[] distances = distance[permutation[i]];
            for (int j = 0; j < dim; j++) {
                sum += prices[j] * distances[permutation[j]];
            }
        }
        return sum;
    }
}
