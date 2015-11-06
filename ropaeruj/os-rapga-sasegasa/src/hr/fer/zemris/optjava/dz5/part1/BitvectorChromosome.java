package hr.fer.zemris.optjava.dz5.part1;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 11/5/15.
 */
public class BitvectorChromosome {
    public double fitness;
    public final byte[] values;
    public final int bits;

    private BitvectorChromosome(int n, int bits) {
        this.bits = bits;
        values = new byte[n];
    }

    public void randomize(Random random) {
        random.nextBytes(values);
    }

    public BitvectorChromosome newLikeThis() {
        return new BitvectorChromosome(values.length, bits);
    }

    public static BitvectorChromosome create(int bitDim) {
        return new BitvectorChromosome((bitDim + 7) >>> 3, bitDim);
    }

    public void mutate(Random random, double degree) {
        for (int i = 0; i < values.length; i++) {
            byte b = 0;
            for (int j = 0; j < Byte.SIZE; j++) {
                b <<= 1;
                if (random.nextDouble() < degree) {
                    b |= 1;
                }
            }
            values[i] ^= b;
        }
    }

    @Override
    public String toString() {
        int cnt=0;
        for (int i = 0; i < bits; i++) {
            if ((values[i >>> 3] & (1 << (i & 0x7))) != 0) {
                cnt++;
            }
        }
        return Integer.toString(cnt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitvectorChromosome that = (BitvectorChromosome) o;

        return Arrays.equals(values, that.values);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
