package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.LinAlgUtils;
import hr.fer.zemris.apr.dz1.Matrix;

/**
 * Created by ivan on 12/6/15.
 */
public class DerivationMethod implements IOptimizingMethod {

    public DerivationMethod(Method method, Type type) {
        this(method, type, Config.PRECISION_6);
    }

    public enum Method {
        NEWTON_RAPHSON,
        GRADIENT_DESCENT
    }

    public enum Type {
        BY_GRAD_LENGTH,
        OPTIMIZATION_ON_LINE
    }

    private Method mMethod;
    private Type mType;
    private double mEpsilon;

    public DerivationMethod(Method method, Type type, double epsilon) {
        mMethod = method;
        mType = type;
        mEpsilon = epsilon;
    }

    @Override
    public Point findMinimum(AbstractFunction f, Point initialPoint) {
        assert f instanceof AFTOWithDerivatives;
        AFTOWithDerivatives func = (AFTOWithDerivatives) f;

        GoldenSectionMethod gsm = new GoldenSectionMethod();
        Point point = initialPoint;

        int lastSeen = 0;
        Point bestPoint = initialPoint;

        while (true) {

//            printPoint(point,f,"Point",3);

            Point solution = func.gradientAt(point).multiply(-1);
            if (mMethod == Method.NEWTON_RAPHSON) {
                Matrix hessian = func.hessianAt(point);
                solution = Point.fromMatrix(LinAlgUtils.solveSystem(hessian, Matrix.fromVector(solution), LinAlgUtils.Method.LUP));
            }

            double norm = solution.norm();

            if (norm < mEpsilon || lastSeen > 100) {
                break;
            }
            switch (mType) {
                case BY_GRAD_LENGTH:
                    point = point.plus(solution);
                    break;
                case OPTIMIZATION_ON_LINE:
                    final Point x0 = point;
                    final Point s = solution;
                    double bestLambda = gsm.findMinimum(new AbstractFunction() {
                        @Override
                        protected double internalValueAt(Point l) {
                            return func.valueAt(x0.plus(s.multiply(l.get(0))));
                        }
                    }, Point.of(0)).get(0);
                    point = x0.plus(solution.multiply(bestLambda));
                    break;
            }
            if (f.valueAt(bestPoint) > f.valueAt(point)) {
                bestPoint = point;
                lastSeen = 0;
            } else {
                lastSeen++;
            }
        }

        return point;
    }

    @Override
    public void setVerbosity(boolean isVerbose) {

    }

    @Override
    public void setTimeout(long time) {

    }
}
