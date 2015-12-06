package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.IVector;
import hr.fer.zemris.apr.dz1.Matrix;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Created by ivan on 11/8/15.
 */
public class Point implements IVector {
    public final double[] values;

    private Point(double[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Point size must be > 0");
        }
        this.values = values;
    }

    public Point copy() {
        return new Point(Arrays.copyOf(values, values.length));
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

    public double sum() {
        return stream().sum();
    }

    public double sumOfSquares() {
        return stream().map(x -> x * x).sum();
    }

    public double average() {
        return stream().average().getAsDouble();
    }

    public double min() {
        return stream().min().getAsDouble();
    }

    public double max() {
        return stream().max().getAsDouble();
    }

    private DoubleStream stream() {
        return Arrays.stream(values);
    }

    public void fill(double value) {
        Arrays.fill(values, value);
    }

    public void set(int i, double value) {
        assert i < values.length;

        values[i] = value;
    }

    public Point multiply(double factor) {
        return unaryOperation(x -> x * factor);
    }

    public Point plus(Point other) {
        return binaryOperation(other, (a, b) -> a + b);
    }

    public Point binaryOperation(Point other, DoubleBinaryOperator operator) {
        int n = this.getDimension();
        assert n == other.getDimension();

        double[] values = new double[n];
        for (int i = 0; i < this.getDimension(); i++) {
            values[i] = operator.applyAsDouble(this.values[i], other.values[i]);
        }
        return new Point(values);
    }

    public Point unaryOperation(DoubleUnaryOperator operator) {
        int n = this.getDimension();

        double[] values = new double[n];
        for (int i = 0; i < this.getDimension(); i++) {
            values[i] = operator.applyAsDouble(this.values[i]);
        }
        return new Point(values);
    }

    public Point limitFromAbove(double maxVal){
        return unaryOperation(a->Math.min(a,maxVal));
    }

    public Point limitFromBelow(double minVal){
        return unaryOperation(a->Math.max(a,minVal));
    }

    public Point reflect(Point center, double alpha) {
        return binaryOperation(center, (a, b) -> (1 + alpha) * b - alpha * a);
    }

    public Point expand(Point reflexion, double gamma) {
        return binaryOperation(reflexion, (a, b) -> (1 - gamma) * a + gamma * b);
    }

    public Point contract(Point center, double beta) {
        return binaryOperation(center, (a, b) -> (1 - beta) * b + beta * a);
    }

    public Point shrink(Point point, double sigma) {
        return binaryOperation(point, (a, b) -> sigma * (a + b));
    }

    public boolean equals(Point other, double precision) {
        return Math.sqrt(minus(other).sumOfSquares()) < precision;
    }

    public Point minus(Point other) {
        return binaryOperation(other, (a, b) -> a - b);
    }

    public String toString(int precision) {
        if (values == null)
            return "null";
        int iMax = values.length - 1;
        if (iMax == -1)
            return "[]";

        DecimalFormat df = new DecimalFormat("0.0");
        df.setMinimumFractionDigits(precision);

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(df.format(values[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    public double distanceTo(Point other) {
        return Math.sqrt(minus(other).sumOfSquares());
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

    public double norm() {
        return Math.sqrt(sumOfSquares());
    }

    public static Point fromMatrix(Matrix matrix) {
        assert matrix.isColumnVector();
        int rowDimension = matrix.getRowDimension();
        double[] underlying = new double[rowDimension];
        for (int i = 0; i < rowDimension; i++) {
            underlying[i] = matrix.get(i, 0);
        }
        return new Point(underlying);
    }

    public Point plus(double value) {
        return unaryOperation(x->x+value);
    }

    public boolean isInfiniteOrNan() {
        for (double v : values) {
            if(Double.isInfinite(v) || Double.isNaN(v)){
                return true;
            }
        }
        return false;
    }
}
