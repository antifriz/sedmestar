package hr.fer.zemris.optjava.dz5.part1;

import java.util.*;

public class GeneticAlgorithm {

    public static final double MUTATION_FACTOR = 0.00;
    public static final double MAX_EFFORT = 1000;
    public static final int MIN_SIZE = 2;
    public static final int MAX_SIZE = 1000;
    public static double COMP_FACTOR = 0;
    public static final int K_TOURNAMENT_CONSTANT = 2;

    public static void main(String[] args) {

        run(Integer.valueOf(args[0]));
    }

    private static boolean run(Integer dimSize) {
        final Random random = new Random();

        Set<BitvectorChromosome> oldPopulation = new HashSet<>();

        for (int i = 0; i < MAX_SIZE; i++) {
            BitvectorChromosome bitvectorChromosome = BitvectorChromosome.create(dimSize);
            bitvectorChromosome.randomize(random);
            oldPopulation.add(bitvectorChromosome);
            calculateScore(bitvectorChromosome);
        }


        Set<BitvectorChromosome> succPopulation = new HashSet<>();

        while (true) {

            succPopulation.clear();

            for (int effort = 0; succPopulation.size() < MAX_SIZE; effort++) {
                if (effort == MAX_EFFORT) {
                    if (succPopulation.size() < MIN_SIZE) {
                        double first = succPopulation.stream().mapToDouble(x -> x.fitness).max().orElse(0);
                        double second = oldPopulation.stream().mapToDouble(x->x.fitness).max().orElse(0);
                        return Math.max(first,second) == 1;
                    }
                    break;
                }

                BitvectorChromosome mama = pickMama(oldPopulation, random);
                BitvectorChromosome papa = pickPapa(oldPopulation, random);

                BitvectorChromosome child = createChild(mama, papa, random);

                //child.mutate(random, MUTATION_FACTOR); //MUTATION_FACTOR == 0

                if (isChildBetter(mama, papa, child, COMP_FACTOR)) {
                    succPopulation.add(child);
                }

            }
            int size = succPopulation.size();
            double someFitness = succPopulation.iterator().next().fitness;
            System.out.printf("%5d %s %4.2f %s\n", size, ProgressBarBuilder.progress(size, MAX_SIZE, 20), someFitness, ProgressBarBuilder.progress(someFitness, 1, 20));

            Set<BitvectorChromosome> k = oldPopulation;
            oldPopulation = succPopulation;
            succPopulation = k;
        }
    }

    private static boolean isChildBetter(BitvectorChromosome mama, BitvectorChromosome papa, BitvectorChromosome child, double compFactor) {
        calculateScore(child);

        double minimal = Math.min(mama.fitness, papa.fitness);
        double maximal = Math.max(mama.fitness, papa.fitness);

        //System.out.printf("%f + %f -> %f",minimal,maximal,child.fitness);

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
        BitvectorChromosome[] bitvectorChromosomes = new BitvectorChromosome[K_TOURNAMENT_CONSTANT];
        for (int i = 0; i < K_TOURNAMENT_CONSTANT; i++) {
            bitvectorChromosomes[i] = randomSelection(oldPopulation,random);
            calculateScore(bitvectorChromosomes[i]);
        }
        double maximal = bitvectorChromosomes[0].fitness;
        int idx=0;
        for (int i = 1; i < K_TOURNAMENT_CONSTANT; i++) {
            if(maximal<bitvectorChromosomes[i].fitness){
                idx = i;
                maximal = bitvectorChromosomes[i].fitness;
            }
        }
        return bitvectorChromosomes[idx];
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
