package hr.fer.zemris.ropaeruj.dz8;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by ivan on 12/9/15.
 */
public class DiffEvoAlg {

    private ANNEvaluator mEvaluator;

    public void setEvaluator(ANNEvaluator evaluator) {
        mEvaluator = evaluator;
    }

    public double[] run(int populationSize, final Random random, double crossoverProbability, int maxIterCount, double desiredError) {
        assert mEvaluator!=null;

        Chromosome[] population = IntStream.range(0, populationSize).mapToObj(x -> Chromosome.createNormalizedRandom(random)).map(this::evaluate).toArray(Chromosome[]::new);
        Chromosome[] newPopulation = new Chromosome[populationSize];

        int dimension =mEvaluator.getSolutionDimension();

        int counter = 0;
        while (!isStopConditionSatisfied(++counter, maxIterCount, population, desiredError)) {
            for (int i = 0; i < populationSize; i++) {
                int r0 = pickUnseenIndex(random, populationSize, i);
                int r1 = pickUnseenIndex(random, populationSize, i, r0);
                int r2 = pickUnseenIndex(random, populationSize, i, r0, r1);

                Chromosome mutantVector = population[r0].duplicate();
                Chromosome bVector = population[r1];
                Chromosome cVector = population[r2];

                Chromosome goalVector = population[i];

                // differential mutation
                for (int j = 0; j < dimension; j++) {
                    mutantVector.genes[j] += random.nextDouble() * (bVector.genes[j] - cVector.genes[j]);
                }

                // crossover
                int freezedIndex = random.nextInt(dimension);
                for (int j = 0; j < dimension; j++) {
                    if (random.nextDouble() >= crossoverProbability && j != freezedIndex) {
                        mutantVector.genes[j] = goalVector.genes[j];
                    }
                }
                evaluate(mutantVector);

                // selection
                newPopulation[i] = mutantVector.error <= goalVector.error ? mutantVector : goalVector;
            }
            population = newPopulation;
        }

        return Arrays.stream(population).min((a,b)->Double.compare(a.error,b.error)).get().genes;
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
        for (int i = 0; i < desiredError; i++) {
            if (population[i].error <= desiredError) {
                return true;
            }
        }
        return false;
    }

}
