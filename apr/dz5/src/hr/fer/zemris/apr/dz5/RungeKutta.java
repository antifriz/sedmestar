package hr.fer.zemris.apr.dz5;

import hr.fer.zemris.apr.dz1.Matrix;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ivan on 1/17/16.
 */
public class RungeKutta extends DiffMethod {

    RungeKutta(Matrix A, Matrix B, Matrix X0, double T, double tmax, int iterStep) {
        super(A, B, X0, T, tmax, iterStep);
    }

@Override
    protected Matrix calculateXk1(Matrix xk) {
        Matrix m1 = A.times(xk).plusEquals(B);
        Matrix m2 = A.times(xk.plus(m1.times(T / 2))).plusEquals(B);
        Matrix m3 = A.times(xk.plus(m2.times(T / 2))).plusEquals(B);
        Matrix m4 = A.times(xk.plus(m3.times(T / 2))).plusEquals(B);

        return xk.plus(m1.plus(m2.times(2)).plusEquals(m3.times(2)).plusEquals(m4).timesEquals(T / 6));
    }
}
