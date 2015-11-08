package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/8/15.
 */
public interface IOptimizingMethod {
    Point findMinimum(IFunction f, Point initialPoint);
}
