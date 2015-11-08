package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/8/15.
 */
public interface IOptimizingMethod {
    Point findMinimum(IFunction f, Point initialPoint);

    default void printPoint(Point p, IFunction f, String title, int precision) {
        System.out.println(title + ": " + pointToString(p, f, precision));
    }

    default String pointToString(Point p, IFunction f, int precision) {
        return String.format("%s -> %f", p.toString(precision), f.valueAt(p));
    }
}
