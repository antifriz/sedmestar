package hr.fer.zemris.optjava.dz5.part2;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by ivan on 11/7/15.
 */
class PermutationChromosome {
    final int[] values;

    double fitness;

    private PermutationChromosome(int n) {
        values = new int[n];
    }


    public PermutationChromosome(int n, Random random, Function<int[], Double> fitnessCalculator) {
        this(n);
        Arrays.setAll(values, i -> i);
        shuffle(values, random);
        fitness = fitnessCalculator.apply(values);
    }

    public PermutationChromosome(int[] ints, Function<int[], Double> fitnessCalculator) {
        values = ints;
        fitness = fitnessCalculator.apply(values);
    }

    static void shuffle(int[] arr, Random random) {
        int size = arr.length;

        for (int i = size; i > 1; i--)
            swap(arr, i - 1, random.nextInt(i));
    }

    static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermutationChromosome that = (PermutationChromosome) o;

        return Arrays.equals(values, that.values);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "PermutationChromosome{" +
                "values=" + Arrays.toString(values) +
                ", value=" +(int) Math.round(-fitness) +
                '}';
    }
}
