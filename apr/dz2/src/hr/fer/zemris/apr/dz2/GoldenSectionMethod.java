package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/7/15.
 */
public class GoldenSectionMethod {

    private static final double DEFAULT_K = 0.5 * Math.sqrt(5) - 0.5;
    private static final double DEFAULT_E = Math.pow(10, -6);
    private final double k = DEFAULT_K;
    private final double e;

    public GoldenSectionMethod(double e) {
        this.e = e;
    }

    public GoldenSectionMethod() {
        this.e = DEFAULT_E;
    }

    public class Interval {
        public final double left;
        public final double right;

        public Interval(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    public double findOptima(IFunction1D f, double h, double point, boolean isUnimodal) {
        Interval interval;
        if (!isUnimodal) {
            interval = unimodalInterval(f, h, point);
        } else {
            interval = new Interval(point - h, point + h);
        }
        return goldenMean(f, interval.left, interval.right);
    }

    Interval unimodalInterval(IFunction1D f, double h, double point) {
        double left = point - h;
        double right = point + h;
        double m = point;
        double fl, fm, fr;
        int step = 1;

        fm = f.valueAt(point);
        fl = f.valueAt(left);
        fr = f.valueAt(right);

        if (fm < fr && fm < fl)
            return new Interval(left, right);
        else if (fm > fr)
            do {
                left = m;
                m = right;
                fm = fr;
                right = point + h * (step *= 2);
                fr = f.valueAt(right);
            } while (fm > fr);
        else
            do {
                right = m;
                m = left;
                fm = fl;
                left = point - h * (step *= 2);
                fl = f.valueAt(left);
            } while (fm > fl);
        return new Interval(left, right);
    }

    double goldenMean(IFunction1D f, double a, double b) {
        double c = b - k * (b - a);
        double d = a + k * (b - a);
        double fc = f.valueAt(c);
        double fd = f.valueAt(d);

        while ((b - a) > e) {
            if (fc < fd) {
                b = d;
                d = c;
                c = b - k * (b - a);
                fd = fc;
                fc = f.valueAt(c);
            } else {
                a = c;
                c = d;
                d = a + k * (b - a);
                fc = fd;
                fd = f.valueAt(d);
            }
        }
        return (a + b) / 2;
    }
}
