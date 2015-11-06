package hr.fer.zemris.optjava.dz5.part1;

import java.util.*;

public class GeneticAlgorithm {

    public static final int DIM_SIZE = 100;

    public static void main(String[] args) {
        // write your code here
        run();
    }

    private static void run() {


        final double maxEffort = 200000;
        final int minSize = 2;
        final int maxSize = 10000;

        final Random random = new Random();

        double compFactor = 1;

        Comparator<BitvectorChromosome> bitvectorChromosomeComparator = (o1, o2) -> {
            if (Arrays.equals(o1.values, o2.values)) {
                return 0;
            }
            return Double.compare(o2.fitness, o1.fitness);
        };
        Set<BitvectorChromosome> oldPopulation = new TreeSet<>(bitvectorChromosomeComparator);

        for (int i = 0; i < maxSize; i++) {
            BitvectorChromosome bitvectorChromosome = BitvectorChromosome.create(DIM_SIZE);
            bitvectorChromosome.randomize(random);
            oldPopulation.add(bitvectorChromosome);
        }


        Set<BitvectorChromosome> succPopulation = new TreeSet<>(bitvectorChromosomeComparator);

        while (true) {
            succPopulation.clear();

            for (int effort = 0; succPopulation.size() < maxSize; effort++) {
                if (effort == maxEffort) {
                    if (succPopulation.size() < minSize) {
                        return;
                    }
                    break;
                }

                BitvectorChromosome mama = pickMama(oldPopulation, random);
                BitvectorChromosome papa = pickPapa(oldPopulation, random);

                BitvectorChromosome child = createChild(mama, papa, random);

                child.mutate(random, 0.05);

                if (isChildBetter(mama, papa, child, compFactor)) {
                    succPopulation.add(child);
                }
            }
            System.out.printf("%d %f\n", succPopulation.size(), succPopulation.iterator().next().fitness);

            Set<BitvectorChromosome> k = oldPopulation;
            oldPopulation = succPopulation;
            succPopulation = k;
        }
    }

    private static boolean isChildBetter(BitvectorChromosome mama, BitvectorChromosome papa, BitvectorChromosome child, double compFactor) {
        calculateScore(child);

        double minimal = Math.min(mama.fitness, papa.fitness);
        double maximal = Math.max(mama.fitness, papa.fitness);

        return child.fitness > minimal + (maximal - minimal) * compFactor;
    }

    private static void calculateScore(BitvectorChromosome child) {
        double cnt = 0;
        for (int i = 0; i < child.bits; i++) {
            if ((child.values[i >>> 3] & (1 << (i & 0x7))) != 0) {
                cnt++;
            }
        }
        double factor = cnt / (double) child.bits;

        if (factor <= 0.8) {
            child.fitness = factor;
        } else if (factor > 0.9) {
            child.fitness = factor * 2 - 1;
        } else {
            child.fitness = 0.8;
        }
    }

    private static BitvectorChromosome createChild(BitvectorChromosome mama, BitvectorChromosome papa, Random random) {
        BitvectorChromosome child = mama.newLikeThis();
        BitvectorChromosome rnd = mama.newLikeThis();
        rnd.randomize(random);
        for (int i = 0; i < child.values.length; i++) {
            child.values[i] = (byte) ((mama.values[i] & papa.values[i]) | (rnd.values[i] & (mama.values[i] ^ papa.values[i])));
        }
        return child;
    }

    private static BitvectorChromosome pickPapa(Set<BitvectorChromosome> oldPopulation, Random random) {
        return kTournamentSelection(oldPopulation, random);
    }

    private static BitvectorChromosome pickMama(Set<BitvectorChromosome> oldPopulation, Random random) {
        return randomSelection(oldPopulation, random);
    }

    private static BitvectorChromosome kTournamentSelection(Set<BitvectorChromosome> oldPopulation, Random random) {
        double sum = 0;
        for (BitvectorChromosome chromosome : oldPopulation) {
            sum += chromosome.fitness;
        }

        //double sum = oldPopulation.parallelStream().mapToDouble(x -> x.fitness).sum();

        if (sum == 0) {
            return randomSelection(oldPopulation, random);
        }
        //double tmpSum = sum;
        double picker = random.nextDouble() * sum;

        for (BitvectorChromosome chromosome : oldPopulation) {
            sum -= chromosome.fitness;
            if (sum <= picker) {
                return chromosome;
            }
        }
        throw new RuntimeException();
    }

    private static BitvectorChromosome randomSelection(Set<BitvectorChromosome> oldPopulation, Random random) {
        Iterator<BitvectorChromosome> it = oldPopulation.iterator();
        int idx = random.nextInt(oldPopulation.size());
        for (int i = 0; i < idx; i++) {
            it.next();
        }
        return it.next();
    }
}
