package hr.fer.zemris.apr.dz3;


/**
 * Created by ivan on 11/7/15.
 */
public class Functions {

    private static AbstractFunctionToOptimize[] functions = new AbstractFunctionToOptimize[]{
            new AbstractFunctionToOptimize() {
                @Override
                public double internalValueAt(Point point) {
                    assert point.getDimension() == 2;
                    return 100 * Math.pow((point.get(1) - Math.pow(point.get(0), 2)), 2) + Math.pow(1 - point.get(0), 2);
                }

                @Override
                public Point startingPoint(int n) {
                    assert n == 2;

                    return Point.of(-1.9, 2);
                }

                @Override
                public Point minimumAt(int n) {
                    assert n == 2;

                    return Point.of(1, 1);
                }

                @Override
                public int dimension(int desired) {
                    return 2;
                }
            },
            new AbstractFunctionToOptimize() {
                @Override
                public double internalValueAt(Point point) {
                    assert point.getDimension() == 2;

                    return Math.pow(point.get(0) - 4, 2) + 4 * Math.pow(point.get(1) - 2, 2);
                }

                @Override
                public Point startingPoint(int n) {
                    return Point.of(-1.9, 2);
                }

                @Override
                public Point minimumAt(int n) {
                    return Point.of(4, 2);
                }

                @Override
                public int dimension(int desired) {
                    return 2;
                }
            },
            new AbstractFunctionToOptimize() {
                @Override
                public double internalValueAt(Point point) {
                    double sum = 0;
                    for (int i = 0; i < point.getDimension(); i++) {
                        sum += (point.get(i) - i - 1) * (point.get(i) - i - 1);
                    }
                    return sum;
                }

                @Override
                public Point startingPoint(int n) {
                    return Point.zeros(n);
                }

                @Override
                public Point minimumAt(int n) {
                    return Point.from(1, n, x -> x);
                }
            },
            new AbstractFunctionToOptimize() {
                @Override
                public double internalValueAt(Point point) {
                    assert point.getDimension() == 2;

                    return Math.abs((point.get(0) - point.get(1)) * (point.get(0) + point.get(1))) + Math.sqrt(point.get(0) * point.get(0) + point.get(1) * point.get(1));
                }

                @Override
                public Point startingPoint(int n) {
                    assert n == 2;

                    return Point.of(5.1, 1.1);
                }

                @Override
                public int dimension(int desired) {
                    return 2;
                }
            },
            new AbstractFunctionToOptimize() {
                @Override
                public double internalValueAt(Point point) {
                    double sumSquares = point.sumOfSquares();
                    return 0.5 + (Math.pow(Math.sin(sumSquares), 2) - 0.5) / Math.pow(1 + 0.001 * sumSquares, 2);
                }

                @Override
                public Point startingPoint(int n) {
                    return Point.random(n);
                }
            },
    };


    public static AbstractFunctionToOptimize get(int idx) {
        return functions[idx];
    }

    public static int size() {
        return functions.length;
    }

}
