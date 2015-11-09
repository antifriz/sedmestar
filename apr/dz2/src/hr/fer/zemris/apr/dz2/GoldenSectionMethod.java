package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/7/15.
 */
public class GoldenSectionMethod implements IOptimizingMethod {

    private static final double DEFAULT_K = 0.5 * Math.sqrt(5) - 0.5;
    private static final double DEFAULT_E = Math.pow(10, -6);
    public static final int DEFAULT_H = 1;
    public double k = DEFAULT_K;
    public double e = DEFAULT_E;
    public double h = DEFAULT_H;

    @Override
    public Point findMinimum(AbstractFunction f, Point initialPoint) {
        assert initialPoint.getDimension() == 1;
        return Point.of(findOptima(f, initialPoint.get(0), false));
    }

    @Override
    public void setVerbosity(boolean isVerbose) {

    }

    @Override
    public void setTimeout(long time) {

    }

    public class Interval {
        public final double left;
        public final double right;

        public Interval(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    public double findOptima(AbstractFunction fun, double point, boolean isUnimodal) {
        // AbstractFunction f = new ProxyFunction(fun);
        Interval interval;
        if (!isUnimodal) {
            interval = unimodalInterval(fun, h, point);
        } else {
            interval = new Interval(point - h, point + h);
        }
        return goldenMean(fun, interval.left, interval.right);
    }

    private Interval unimodalInterval(AbstractFunction f, double h, double point) {
        double left = point - h;
        double right = point + h;
        double m = point;
        double fl, fm, fr;
        int step = 1;

        fm = value(f, point);
        fl = value(f, left);
        fr = value(f, right);

        if (fm < fr && fm < fl)
            return new Interval(left, right);
        else if (fm > fr)
            do {
                left = m;
                m = right;
                fm = fr;
                right = point + h * (step *= 2);
                fr = value(f, right);
            } while (fm > fr);
        else
            do {
                right = m;
                m = left;
                fm = fl;
                left = point - h * (step *= 2);
                fl = value(f, left);
            } while (fm > fl);
        return new Interval(left, right);
    }

    private double goldenMean(AbstractFunction f, double a, double b) {
        double c = b - k * (b - a);
        double d = a + k * (b - a);
        double fc = value(f, c);
        double fd = value(f, d);

        while ((b - a) > e) {
            if (fc < fd) {
                b = d;
                d = c;
                c = b - k * (b - a);
                fd = fc;
                fc = value(f, c);
            } else {
                a = c;
                c = d;
                d = a + k * (b - a);
                fc = fd;
                fd = value(f, d);
            }
        }
        return (a + b) / 2;
    }

    private double value(AbstractFunction f, double point) {
        return f.valueAt(Point.of(point));
    }
}
