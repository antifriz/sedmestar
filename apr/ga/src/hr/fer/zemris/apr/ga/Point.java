package hr.fer.zemris.apr.ga;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Created by ivan on 11/8/15.
 */
public class Point {
    public final double[] values;

    public Point(double[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Point size must be > 0");
        }
        this.values = values;
    }

    public int getDimension() {
        return values.length;
    }

    public double get(int i) {
        return values[i];
    }

    public static Point of(double... values) {
        return new Point(Arrays.copyOf(values, values.length));
    }

    public static Point zeros(int n) {
        return new Point(new double[n]);
    }

    public static Point from(int seed, int n, IntToDoubleFunction iterator) {
        return new Point(IntStream.range(seed, n + seed).mapToDouble(iterator::applyAsDouble).toArray());
    }

    public static Point random(int n) {
        return new Point(ThreadLocalRandom.current().doubles(n).toArray());
    }

    public double sumOfSquares() {
        return stream().map(x -> x * x).sum();
    }

    private DoubleStream stream() {
        return Arrays.stream(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return Arrays.equals(values, point.values);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
