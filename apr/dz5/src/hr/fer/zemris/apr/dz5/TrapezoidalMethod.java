package hr.fer.zemris.apr.dz5;

import hr.fer.zemris.apr.dz1.Matrix;

/**
 * Created by ivan on 1/17/16.
 */
public class TrapezoidalMethod extends DiffMethod {
    Matrix R;
    Matrix S;
    public TrapezoidalMethod(Matrix A, Matrix B, Matrix X0, double T, double tmax, int iterStep) {
        super(A, B, X0, T, tmax, iterStep);
        Matrix I = Matrix.createIdentity(A);
        Matrix A_ = A.times(T/2);
        Matrix tmp1 = I.minus(A_);
        Matrix tmp2 = I.plusEquals(A_);
        Matrix inverse = tmp1.inverse();
        R = inverse.times(tmp2);
        S= inverse.times(B).timesEquals(T/2);
    }

    @Override
    protected Matrix calculateXk1(Matrix xk) {
        return R.times(xk).plusEquals(S);
    }
}
