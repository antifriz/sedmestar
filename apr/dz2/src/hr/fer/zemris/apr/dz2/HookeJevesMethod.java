package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/8/15.
 */
public class HookeJevesMethod implements IOptimizingMethod {
    private double dx = 0.5;
    private double precision = Config.PRECISION_6;

    @Override
    public Point findMinimum(IFunction f, Point initialPoint) {
        Point p = initialPoint.copy();
        Point b = initialPoint.copy();
        int i = 0;
        do {
            Point n = explore(p, dx, f);
            int precision = 5;
            System.out.printf("%s %s %s\n", pointToString(b, f, precision), pointToString(p, f, precision), pointToString(n, f, precision));
            if (f.valueAt(n) < f.valueAt(b)) {
                p = n.multiply(2).minus(b);
                b = n;
            } else {
                dx = tweak(dx);
                p = b;
            }
            i++;
        } while (Math.sqrt(p.minus(b).sumOfSquares()) < precision);
        printPoint(b,f,String.format("====================\nNumber of iterations: %d\nBest",i),5);
        return b;
    }

    private double tweak(double dx) {
        return dx;
    }

    private Point explore(Point x, double dx, IFunction f) {
        for (int i = 0; i < x.getDimension(); i++) {
            double p = f.valueAt(x);

            x.set(i, x.get(i) + dx);
            double n = f.valueAt(x);

            if (n > p) {
                x.set(i, x.get(i) - 2 * dx);
                n = f.valueAt(x);
                if (n > p) {
                    x.set(i, x.get(i) + dx);
                }
            }
        }
        return x;
    }


}
