package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.util.Arrays;

/**
 * Created by ivan on 10/18/15.
 * <p>
 * Implementation of algorithms used in numeric optimization
 */
public class NumOptAlgorithms {

    // ovisno koliko precizni zelimo biti (preciznost ~ vrijeme)
    public static final double EQUALITY_RATE = Math.pow(10, -5);
    public static final double EQUALITY_RATE_LAMBDA = Math.pow(10, -9);
    public static double upperBound = 1.0;

    /**
     * gradient descent impl
     *
     * @param function     function to optimize
     * @param maxIterCount maximum allowed iteration count
     * @return resulting minimum
     */
    public static Matrix gradientDescent(Matrix initialSolution, IFunction function, int maxIterCount, OnStepListener listener) {
        return runAlgorithm(initialSolution, function, maxIterCount, listener, Algorithm.GRADIENT_DESCENT);
    }

    private static boolean gradientIsConsideredZero(Matrix gradient) {
        return Arrays.stream(gradient.getRowPackedCopy()).allMatch(x -> isConsideredEqual(x, 0.0));
    }

    /**
     * newton method impl
     *
     * @param function     function to optimize
     * @param maxIterCount maximum allowed iteration count
     * @return resulting minimum
     */
    public static Matrix newtonMethod(Matrix initialSolution, IHFunction function, int maxIterCount, OnStepListener listener) {
        return runAlgorithm(initialSolution, function, maxIterCount, listener, Algorithm.NEWTON_METHOD);
    }

    public static Matrix runAlgorithm(Matrix initialSolution, IFunction function, int maxIterCount, OnStepListener listener, Algorithm algorithm) {
        Matrix currentOptimalPoint = initialSolution;
        System.out.printf("Starting algorithm %s with initial point: %s\n", algorithm.name(), MatrixUtils.prettyPrintVector(currentOptimalPoint));
        for (int i = 1; i <= maxIterCount; i++) {
            Matrix gradient = function.gradientAt(currentOptimalPoint);
            if (listener != null) {
                listener.onStepEntered(currentOptimalPoint);
            }

            if (gradientIsConsideredZero(gradient)) {
                System.out.printf("[%4d] best: %s gradient: %s error: %f\n", i, MatrixUtils.prettyPrintVector(currentOptimalPoint), MatrixUtils.prettyPrintVector(gradient), gradient.normF());
                break;
            }
            Matrix searchDirection;
            switch (algorithm) {
                case NEWTON_METHOD:
                    searchDirection = ((IHFunction) function).hessianAt(currentOptimalPoint).inverse().times(gradient).times(-1);
                    break;
                case GRADIENT_DESCENT:
                default:
                    searchDirection = gradient.times(-1);
                    break;
            }
            double lambda = getLambdaByBisection(currentOptimalPoint, function, searchDirection);
            System.out.printf("[%4d] best: %s gradient: %s %6.4f direction: %s lambda: %f\n", i, MatrixUtils.prettyPrintVector(currentOptimalPoint), MatrixUtils.prettyPrintVector(gradient), gradient.normF(), MatrixUtils.prettyPrintVector(searchDirection), lambda);
            currentOptimalPoint = currentOptimalPoint.plus(searchDirection.times(lambda));
        }
        return currentOptimalPoint;
    }

    private static double getLambdaByBisection(Matrix currentPoint, IFunction function, Matrix searchDirection) {
        upperBound = 0.0001;
        double lambdaLowerBound = 0.0, lambdaUpperBound = upperBound;
        while (true) {
            Matrix pointOfInterest = currentPoint.plus(searchDirection.times(lambdaUpperBound));
            double derivation = getDerivationAlongLine(function, searchDirection, pointOfInterest);

            if (derivation > 0) {
                break;
            } else {
                lambdaUpperBound *= 2;
            }
        }
        double lambdaLower = lambdaLowerBound, lambdaUpper = lambdaUpperBound;
        while (true) {
            double lambdaMid = lambdaLower / 2 + lambdaUpper / 2;
            Matrix pointOfInterest = currentPoint.plus(searchDirection.times(lambdaMid));
            double derivation = getDerivationAlongLine(function, searchDirection, pointOfInterest);
            if (Math.abs(derivation) < EQUALITY_RATE_LAMBDA || lambdaLower >= lambdaMid || lambdaUpper <= lambdaMid) {
                return lambdaMid;
            } else if (derivation > 0) {
                lambdaUpper = lambdaMid;
            } else {
                lambdaLower = lambdaMid;
            }
        }
    }

    private static boolean isConsideredEqual(double value1, double value2) {
        return Math.abs(value1 - value2) < EQUALITY_RATE;
    }

    private static double getDerivationAlongLine(IFunction function, Matrix lineVector, Matrix pointOfInterest) {
        Matrix gradient = function.gradientAt(pointOfInterest);

        Matrix gradientOnLine = lineVector.transpose().times(gradient);
        return MatrixUtils.asScalar(gradientOnLine);
    }

    enum Algorithm {
        NEWTON_METHOD,
        GRADIENT_DESCENT
    }
}
