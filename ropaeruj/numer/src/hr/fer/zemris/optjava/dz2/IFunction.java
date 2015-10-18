package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by ivan on 10/18/15.
 *
 * Scalar function over n-dim vector
 */
public interface IFunction {
    /**
     *
     * @return number of variables over which function is defined
     */
    int numberOfVariables();

    /**
     * calculates value of function for given point
     * @param point
     * @return value of function
     */
    double valueAt(Matrix point);

    /**
     * calculates gradient vector at given point
     * @param point
     * @return gradient vector
     */
    Matrix gradientAt(Matrix point);
}
