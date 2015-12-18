package hr.fer.zemris.ropaeruj.dz8;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created by ivan on 12/9/15.
 */
public class DiffEvoAlg {

    private ANNEvaluator mEvaluator;

    public void setEvaluator(ANNEvaluator evaluator) {
        mEvaluator = evaluator;
    }

    public double[] run(int populationSize, final Random random, double crossoverConstant, int maxIterCount, double desiredError) {
        assert mEvaluator != null;

        final int dimension = mEvaluator.getSolutionDimension();
        Chromosome[] population = IntStream.range(0, populationSize).mapToObj(x -> Chromosome.createNormalizedRandom(random, dimension)).map(this::evaluate).toArray(Chromosome[]::new);

        int counter = 0;
        while (!isStopConditionSatisfied(++counter, maxIterCount, population, desiredError)) {
            final Chromosome[] pop = population;
            final Chromosome best = getBest(pop);
            final AtomicInteger newOnes = new AtomicInteger(0);
            population = IntStream.range(0, populationSize).parallel().mapToObj(i -> {
                int r0 = pickUnseenIndex(random, populationSize, i);
                int r1 = pickUnseenIndex(random, populationSize, i, r0);
                int r2 = pickUnseenIndex(random, populationSize, i, r0, r1);

                Chromosome mutantVector = pop[r0].duplicate();
                Chromosome bVector = pop[r1];
                Chromosome cVector = pop[r2];

                bVector = best;

                Chromosome goalVector = pop[i];

                // differential mutation
                for (int j = 0; j < dimension; j++) {
                    mutantVector.genes[j] += random.nextDouble() * (bVector.genes[j] - cVector.genes[j]);
                }

                // crossover
                int freezedIndex = random.nextInt(dimension);
                for (int j = 0; j < dimension; j++) {
                    if (random.nextDouble() >= crossoverConstant && j != freezedIndex) {
                        mutantVector.genes[j] = goalVector.genes[j];
                    }
                }
                evaluate(mutantVector);

                // selection
                if (mutantVector.error <= goalVector.error) {
                    newOnes.incrementAndGet();
                    //System.out.println("Improvement "+(goalVector.error - mutantVector.error));
                    return mutantVector;
                } else {
                    return goalVector;
                }
            }).toArray(Chromosome[]::new);
            if (counter % 100 == 0) {
                System.out.printf("[%5d] Best: %6.4f\n", counter, getBest(population).error);
            }
        }
        System.out.printf("[%5d] Best: %6.4f\n", counter, getBest(population).error);
        return getBest(population).genes;
    }

    private int pickUnseenIndex(Random random, int populationSize, int... blacklist) {
        int index;
        outer:
        do {
            index = random.nextInt(populationSize);
            for (int blacklisted : blacklist) {
                if (blacklisted == index) {
                    continue outer;
                }
            }
            break;
        } while (true);
        return index;
    }

    private Chromosome evaluate(Chromosome chromosome) {
        chromosome.error = mEvaluator.evaluate(chromosome.genes);
        return chromosome;
    }

    public boolean isStopConditionSatisfied(int counter, int maxIterCount, Chromosome[] population, double desiredError) {
        if (counter >= maxIterCount) {
            return true;
        }
        for (Chromosome chromosome : population) {
            if (chromosome.error <= desiredError) {
                return true;
            }
        }
        return false;
    }

    private Chromosome getBest(Chromosome[] population) {
        return Arrays.stream(population).min((a, b) -> Double.compare(a.error, b.error)).get();
    }

    private Chromosome getWorst(Chromosome[] population) {
        return Arrays.stream(population).max((a, b) -> Double.compare(a.error, b.error)).get();
    }

}
