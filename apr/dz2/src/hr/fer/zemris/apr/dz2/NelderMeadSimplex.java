package hr.fer.zemris.apr.dz2;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by ivan on 11/8/15.
 */
public class NelderMeadSimplex implements IOptimizingMethod {

    public double alpha = 1;
    public double beta = 0.5;
    public double gamma = 2;
    public double epsilon = Config.PRECISION_6;
    public double sigma = 0.5;
    public double simplexT = 1;

    public Point findMinimum(IFunction f, Point initialPoint) {

        System.out.printf("Initial point: %s\n====================\n", initialPoint);

        List<Point> d = PointUtils.constructSimplex(initialPoint, simplexT);

        notifyUpdate(d, f);

        int i =0;
        while (true) {
            final Point centroid = PointUtils.centroid(d, getHighest(d));
            printPoint(centroid, f, "Centroid");

            if (Math.sqrt(d.stream().mapToDouble(x -> Math.pow(f.valueAt(x) - f.valueAt(centroid), 2)).sum()) <= d.size() * epsilon) {
                Point best = f.valueAt(centroid) < getLowestValue(d, f) ? centroid : getLowest(d);
                printPoint(best,f,String.format("====================\nNumber of iterations: %d\nBest",i));
                return best;
            }

            Point reflection = getHighest(d).reflect(centroid, alpha);

            if (f.valueAt(reflection) < getLowestValue(d, f)) {
                Point expansion = centroid.expand(reflection, gamma);
                if (f.valueAt(expansion) < getLowestValue(d, f)) {
                    updateLowest(expansion, d, f);
                } else {
                    updateLowest(reflection, d, f);
                }
            } else {
                boolean canBeSecondHighest = d.size() == 1 || f.valueAt(reflection) > f.valueAt(d.get(d.size() - 2));
                if (canBeSecondHighest) {
                    if (f.valueAt(reflection) < getHighestValue(d, f)) {
                        updateHighest(reflection, d, f);
                    }

                    Point contraction = getHighest(d).contract(centroid, beta);
                    if (f.valueAt(contraction) < getHighestValue(d, f)) {
                        updateHighest(contraction, d, f);
                    } else {
                        shrinkSimplex(f, d);
                    }

                } else {
                    updateHighest(reflection, d, f);
                }
            }
            i++;
        }
    }

    private void printSimplex(List<Point> d, IFunction f) {
        System.out.println("Simplex:");
        for (Point p : d) {
            printPoint(p, f, "");
        }
    }

    private PrintStream printPoint(Point p, IFunction f, String title) {
        return System.out.printf("%s: %s -> %f\n", title, p.toString(5), f.valueAt(p));
    }

    private void shrinkSimplex(IFunction f, List<Point> d) {
        for (int i = 1; i < d.size(); i++) {
            d.set(i, d.get(i).shrink(getLowest(d), sigma));
        }
        notifyUpdate(d, f);
    }

    private double getHighestValue(List<Point> d, IFunction f) {
        return f.valueAt(getHighest(d));
    }

    private void updateLowest(Point point, List<Point> d, IFunction f) {
        d.remove(0);
        d.add(0, point);
        notifyUpdate(d, f);
    }

    private void updateHighest(Point point, List<Point> d, IFunction f) {
        d.remove(d.size() - 1);
        d.add(point);
        notifyUpdate(d, f);
    }

    private double getLowestValue(List<Point> d, IFunction f) {
        return f.valueAt(getLowest(d));
    }

    private void notifyUpdate(List<Point> d, IFunction f) {
        Collections.sort(d, (a, b) -> Double.compare(f.valueAt(a), f.valueAt(b)));
    }

    private Point getHighest(List<Point> d) {
        return d.get(d.size() - 1);
    }

    private Point getLowest(List<Point> d) {
        return d.get(0);
    }

}
