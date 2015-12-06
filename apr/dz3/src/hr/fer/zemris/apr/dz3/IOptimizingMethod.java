package hr.fer.zemris.apr.dz3;

/**
 * Created by ivan on 11/8/15.
 */
public interface IOptimizingMethod {
    Point findMinimum(AbstractFunction f, Point initialPoint);

    default void printPoint(Point p, AbstractFunction f, String title, int precision) {
        System.out.println(title + ": " + pointToString(p, f, precision));
    }

    default String pointToString(Point p, AbstractFunction f, int precision) {
        return String.format("%s -> %f", p.toString(precision), f.valueAt(p));
    }

    void setVerbosity(boolean isVerbose);

    void setTimeout(long time);
}
