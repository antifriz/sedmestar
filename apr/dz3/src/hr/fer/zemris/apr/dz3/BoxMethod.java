package hr.fer.zemris.apr.dz3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by ivan on 11/8/15.
 */
public class BoxMethod implements IOptimizingMethod {

    public double alpha = 1.3;
    public double epsilon = Config.PRECISION_6;

    private boolean verbose = false;

    private long timeout = 0;
    public boolean useTweak = false;
    private double mBottomExplicit;
    private double mTopExplicit;
    private Function<Point, Boolean> mImplicitSatisfaction;

    public BoxMethod(double bottomExplicit, double topExplicit, Function<Point, Boolean> implicitSatisfaction) {
        mBottomExplicit = bottomExplicit;
        mTopExplicit = topExplicit;
        mImplicitSatisfaction = implicitSatisfaction;
    }

    public Point findMinimum(AbstractFunction fun, Point initialPoint) {
        AbstractFunction f = new ProxyFunction(fun);

        if (!satisfiyExplicit(initialPoint).equals(initialPoint) || !mImplicitSatisfaction.apply(initialPoint)) {
            System.out.println(initialPoint);
            System.out.println(satisfiyExplicit(initialPoint));
            throw new RuntimeException("Initial point not OK " + !satisfiyExplicit(initialPoint).equals(initialPoint) + " " + !mImplicitSatisfaction.apply(initialPoint));
        }

        if (verbose) System.out.printf("Initial point: %s\n====================\n", initialPoint);


        List<Point> d = constructBox(initialPoint);

        notifyUpdate(d, f);

        Point bestCentroidSoFar = initialPoint;
        int lastSeen = 0;

        long timeStart = System.currentTimeMillis();
        while (true) {
            final Point centroid = PointUtils.centroid(d, getHighest(d));
            if (verbose) printSimplex(d, f);
            if (verbose) printPoint(centroid, f, "Centroid", 5);


            if (lastSeen>100 ||(Math.sqrt(d.stream().mapToDouble(x -> Math.pow(f.valueAt(x) - f.valueAt(centroid), 2)).sum()) <= d.size() * epsilon && (!useTweak || Math.sqrt(d.stream().map(centroid::minus).mapToDouble(Point::sumOfSquares).sum()) <= epsilon * d.size()))) {
                Point best = f.valueAt(centroid) < getLowestValue(d, f) ? centroid : getLowest(d);
                if (verbose)
                    printPoint(best, f, "Best", 5);
                return best;
            }
            if (timeout > 0 && System.currentTimeMillis() - timeStart > timeout) {
                throw new RuntimeException("timeout");
            }

            Point reflection = getHighest(d).reflect(centroid, alpha);

            reflection = satisfiyExplicit(reflection);

            reflection = satisfyImplicit(centroid, reflection);

            if (f.valueAt(d.get(d.size() - 2)) < f.valueAt(reflection)) {
                reflection = reflection.shrink(centroid, 0.5);
            }
            updateHighest(reflection, d, f);

            if(f.valueAt(centroid)<f.valueAt(bestCentroidSoFar)){
                bestCentroidSoFar = centroid;
                lastSeen = 0;
            }
            else {
                lastSeen++;
            }
        }
    }

    private Point satisfiyExplicit(Point point) {
        return point.limitFromAbove(mTopExplicit).limitFromBelow(mBottomExplicit);
    }

    private List<Point> constructBox(Point initialPoint) {
        double count = Math.pow(2, initialPoint.getDimension());
        List<Point> points = new ArrayList<>();
        points.add(initialPoint);
        for (int i = 1; i < count; i++) {
            Point point = Point.random(initialPoint.getDimension());
            point = point.multiply(mTopExplicit - mBottomExplicit).plus(mBottomExplicit);
            point = satisfyImplicit(initialPoint, point);
            points.add(point);
        }
        return points;
    }

    private Point satisfyImplicit(Point center, Point point) {
        while (!mImplicitSatisfaction.apply(point)) {
            point = point.shrink(center, 0.5);
        }
        return point;
    }

    @Override
    public void setVerbosity(boolean isVerbose) {
        verbose = isVerbose;
    }

    @Override
    public void setTimeout(long time) {
        timeout = time;
    }

    private void printSimplex(List<Point> d, AbstractFunction f) {
        System.out.println("Simplex:");
        for (Point p : d) {
            printPoint(p, f, "", 5);
        }
    }

    private void updateHighest(Point point, List<Point> d, AbstractFunction f) {
        d.remove(d.size() - 1);
        d.add(point);
        notifyUpdate(d, f);
    }

    private double getLowestValue(List<Point> d, AbstractFunction f) {
        return f.valueAt(getLowest(d));
    }

    private void notifyUpdate(List<Point> d, AbstractFunction f) {
        Collections.sort(d, (a, b) -> Double.compare(f.valueAt(a), f.valueAt(b)));
    }

    private Point getHighest(List<Point> d) {
        return d.get(d.size() - 1);
    }

    private Point getLowest(List<Point> d) {
        return d.get(0);
    }

}
