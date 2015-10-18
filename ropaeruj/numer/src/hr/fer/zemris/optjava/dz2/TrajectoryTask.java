package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.util.Random;

/**
 * Created by ivan on 10/18/15.
 */
public abstract class TrajectoryTask {
    private final Random mRandom;

    public TrajectoryTask() {
        mRandom = new Random();
    }

    protected abstract Matrix solve(Matrix initialPoint, int maxIterCount);

    public void run(double initialX, double initialY, int maxIterCount) {
        double x1 = initialX;
        double x2 = initialY;
        Matrix initialPoint = new Matrix(new double[][]{{x1}, {x2}});

        Matrix optimalPoint = solve(initialPoint, maxIterCount);
        MatrixUtils.assertIs2DPoint(optimalPoint);
    }
}
