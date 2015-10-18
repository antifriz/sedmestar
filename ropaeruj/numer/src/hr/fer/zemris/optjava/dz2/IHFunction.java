package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by ivan on 10/18/15.
 *
 * IFunction extension that calculates Hessian matrix
 */
public interface IHFunction extends IFunction {
    /**
     * calculates Hessian matrix of a function at given point
     * @param point
     * @return Hessian matrix
     */
    Matrix hessianAt(Matrix point);
}
