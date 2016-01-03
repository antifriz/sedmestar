package hr.fer.zemris.apr.ga;


/**
 * Created by ivan on 11/7/15.
 */
public class Functions {

    private static AbstractFunctionToOptimize[] functions = new AbstractFunctionToOptimize[]{
            null,
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
            null,
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
            null,
            null,
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
            new AbstractFunctionToOptimize() {
                @Override
                public double internalValueAt(Point point) {
                    double sumSquares = point.sumOfSquares();
                    return Math.pow(sumSquares,0.25)*(1+Math.pow(Math.sin(50*Math.pow(sumSquares,0.1)),2));
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

}
