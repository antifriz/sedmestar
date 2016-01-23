package hr.fer.zemris.nenr.zad7;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ivan on 1/23/16.
 */
public class GATrainer implements IFANNTrainer {

    private final FunkyNeuralNetwork funkyNeuralNetwork;
    private final IReadOnlyDataset dataset;
    private final int popSize;
    private final int maxIterCount;
    private final double v1;
    private final double sigma1;
    private final double sigma2;
    private final double mutationPerc;

    public GATrainer(FunkyNeuralNetwork funkyNeuralNetwork, IReadOnlyDataset dataset, int popSize, int maxIterCount, double v1, double sigma1, double sigma2, double mutationPerc) {
        this.funkyNeuralNetwork = funkyNeuralNetwork;
        this.dataset = dataset;
        this.popSize = popSize;
        this.maxIterCount = maxIterCount;
        this.v1 = v1;
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.mutationPerc = mutationPerc;
    }

    @Override
    public double[] trainFFANN() {
        int weightsCount = funkyNeuralNetwork.getWeightsCount();
        Random random = new Random();
        List<Chromosome> population = IntStream.range(0, popSize).mapToObj(i -> new Chromosome(random, weightsCount)).collect(Collectors.toList());

        population.parallelStream().forEach(c -> c.mse = funkyNeuralNetwork.evaluate(c.values,dataset));

        Chromosome best = population.get(0);
        for (int i = 0; i < maxIterCount; i++) {

            population.sort(Comparator.<Chromosome>comparingDouble(c -> c.mse));

            best = population.get(0);
            if(i%10000==0){
                System.out.printf("[%5d] Best: %f \n", i, best.mse);
            }
            int kTournament =3;
            List<Chromosome> cs = new ArrayList<>();
            for (int k = 0; k < kTournament; k++) {
                Chromosome chromosome;
                do {
                    chromosome = population.get(random.nextInt(population.size()));
                } while (cs.contains(chromosome));
                cs.add(chromosome);
            }

            cs.sort(Comparator.<Chromosome>comparingDouble(c -> c.mse));

            Chromosome mama = cs.get(0);
            Chromosome papa = cs.get(1);

            Chromosome child = mama.duplicate();
            switch (random.nextInt(3)) {
                case 0:
                    int breakPoint = random.nextInt(weightsCount);
                    System.arraycopy(papa.values, 0, child.values, 0, breakPoint);
                    break;
                case 1:
                    for (int k = 0; k < weightsCount; k++) {
                        if (random.nextBoolean()) {
                            child.values[k] = papa.values[k];
                        }
                    }
                    break;
                default:
                    for (int k = 0; k < weightsCount; k++) {
                        double a = mama.values[k];
                        double b = papa.values[k];
                        double min = Math.min(a, b);
                        double max = Math.max(a, b);
                        child.values[k] = min + (max - min) * random.nextDouble();
                    }
                    break;
            }

            if (random.nextDouble() <= v1) {
                for (int k = 0; k < weightsCount; k++) {
                    if (random.nextDouble()<=mutationPerc) {
                        child.values[k] += random.nextGaussian() * sigma1;
                    }
                }
            } else {
                for (int k = 0; k < weightsCount; k++) {
                    if (random.nextDouble()<=mutationPerc) {
                        child.values[k] = random.nextGaussian() * sigma2;
                    }
                }
            }
            child.mse = funkyNeuralNetwork.evaluate(child.values,dataset);
            population.remove(cs.get(cs.size()-1));
            population.add(child);
        }
        return best.values;
    }

    class Chromosome {
        double[] values;
        double mse;

        Chromosome(Random random, int n) {
            values = random.doubles(n).map(d -> 2 * d - 1).toArray();
        }

        Chromosome(Chromosome chromosome) {
            values = Arrays.copyOf(chromosome.values, chromosome.values.length);
        }

        Chromosome duplicate() {
            return new Chromosome(this);
        }

    }

}
