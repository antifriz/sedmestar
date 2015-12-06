package hr.fer.zemris.apr.dz3;


import hr.fer.zemris.apr.dz1.Matrix;

/**
 * Created by ivan on 11/7/15.
 */
public class Functions {

    private static AFTOWithDerivatives[] functions = new AFTOWithDerivatives[]{
            new AFTOWithDerivatives() {
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
                protected Point internalGradientAt(Point point) {
                    return Point.of(-400 * (point.get(1) - Math.pow(point.get(0), 2)) * point.get(0)-2*(1-point.get(0)), 200 * (point.get(1) - Math.pow(point.get(0), 2))+2);
                }

                @Override
                protected Matrix internalHessianAt(Point point) {
                    return Matrix.fill(2, 2, 400 * (3*Math.pow(point.get(0), 2) - point.get(1))+2, -400 * point.get(0), -400 * point.get(0), 200);
                }

                @Override
                public int dimension(int desired) {
                    return 2;
                }
            },
            new AFTOWithDerivatives() {
                @Override
                public double internalValueAt(Point point) {
                    assert point.getDimension() == 2;

                    return Math.pow(point.get(0) - 4, 2) + 4 * Math.pow(point.get(1) - 2, 2);
                }

                @Override
                public Point startingPoint(int n) {
                    return Point.of(0.1, 0.3);
                }

                @Override
                public Point minimumAt(int n) {
                    return Point.of(4, 2);
                }

                @Override
                protected Point internalGradientAt(Point point) {
                    return Point.of(2 * (point.get(0) - 4), 8 * (point.get(1) - 2));
                }

                @Override
                protected Matrix internalHessianAt(Point point) {
                    return Matrix.fill(2, 2, 2, 0, 0, 8);
                }


                @Override
                public int dimension(int desired) {
                    return 2;
                }
            },
            new AFTOWithDerivatives() {
                @Override
                public double internalValueAt(Point point) {
                    return Math.pow(point.get(0) - 2, 2) + Math.pow(point.get(1) + 3, 2);
                }

                @Override
                protected Point internalGradientAt(Point point) {
                    return Point.of(2 * (point.get(0) - 2), 2 * (point.get(1) + 3));
                }

                @Override
                protected Matrix internalHessianAt(Point point) {
                    return Matrix.fill(2, 2, 2, 0, 0, 2);
                }

                @Override
                public Point startingPoint(int n) {
                    return Point.zeros(n);
                }

                @Override
                public Point minimumAt(int n) {
                    return Point.of(2, -3);
                }
            },
            new AFTOWithDerivatives() {
                @Override
                public double internalValueAt(Point point) {
                    return Math.pow(point.get(0) - 3, 2) + Math.pow(point.get(1), 2);
                }

                @Override
                protected Point internalGradientAt(Point point) {
                    return Point.of(2 * (point.get(0) - 3), 2 * (point.get(1)));
                }

                @Override
                protected Matrix internalHessianAt(Point point) {
                    return Matrix.fill(2, 2, 2, 0, 0, 2);
                }

                @Override
                public Point startingPoint(int n) {
                    return Point.zeros(n);
                }

                @Override
                public Point minimumAt(int n) {
                    return Point.of(3, 0);
                }
            },
    };


    public static AFTOWithDerivatives get(int idx) {
        return functions[idx];
    }

    public static int size() {
        return functions.length;
    }

}
