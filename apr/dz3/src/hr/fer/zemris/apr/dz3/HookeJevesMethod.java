package hr.fer.zemris.apr.dz3;

/**
 * Created by ivan on 11/8/15.
 */
public class HookeJevesMethod implements IOptimizingMethod {
    public static final double INITIAL_DX = 0.5;
    private double precision = Config.PRECISION_6;

    private boolean verbose = false;

    @Override
    public Point findMinimum(AbstractFunction fun, Point initialPoint) {
        AbstractFunction f = new ProxyFunction(fun);
        Point p = initialPoint.copy();
        Point b = initialPoint.copy();
        double dx = INITIAL_DX;
        do {
            Point n = explore(p, dx, f);
            int precision = 10;
            if (verbose)
                System.out.printf("%8.6f | %s %s %s\n", dx,pointToString(b, f, precision), pointToString(p, f, precision), pointToString(n, f, precision));
            if (f.valueAt(n) < f.valueAt(b)) {
                p = n.multiply(2).minus(b);
                b = n;
            } else {
                dx /= 2;
                p = b;
            }
        } while (dx > precision);
        if (verbose) printPoint(b, f, "Best", 5);
        return b;
    }

    private Point explore(Point po, double dx, AbstractFunction f) {
        Point x = po.copy();
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

    @Override
    public void setVerbosity(boolean isVerbose) {
        verbose = isVerbose;
    }

    @Override
    public void setTimeout(long time) {

    }
}
