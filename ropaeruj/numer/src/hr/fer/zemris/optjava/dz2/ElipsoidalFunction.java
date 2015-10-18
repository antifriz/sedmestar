package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by ivan on 10/18/15.
 */
public class ElipsoidalFunction implements IHFunction {

    @Override
    public Matrix hessianAt(Matrix point) {
        MatrixUtils.assertIs2DPoint(point);
        return new Matrix(new double[][]{{2.0, 0.0}, {0.0, 20.0}});
    }

    @Override
    public int numberOfVariables() {
        return 2;
    }

    @Override
    public double valueAt(Matrix point) {
        MatrixUtils.assertIs2DPoint(point);
        return Math.pow(getX1(point) - 1.0, 2) + 10 * Math.pow(getX2(point) - 2.0, 2);
    }

    @Override
    public Matrix gradientAt(Matrix point) {
        MatrixUtils.assertIs2DPoint(point);
        return new Matrix(new double[][]{{2.0 * (getX1(point) - 1.0)}, {20.0 * (getX2(point) - 2.0)}});
    }

    private double getX2(Matrix point) {
        return point.get(1, 0);
    }

    private double getX1(Matrix point) {
        return point.get(0, 0);
    }
}
