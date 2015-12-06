package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.LinAlgUtils;
import hr.fer.zemris.apr.dz1.Matrix;

/**
 * Created by ivan on 12/6/15.
 */
public class NewtonRaphsonMethod implements IOptimizingMethod {

    enum Type{
        BY_GRAD_LENGTH,
        OPTIMIZATION_ON_LINE
    }

    private Type mType;
    private double mEpsilon;

    public NewtonRaphsonMethod( Type type,double epsilon) {
        mType = type;
        mEpsilon = epsilon;
    }

    @Override
    public Point findMinimum(AbstractFunction f, Point initialPoint) {
        assert f instanceof AFTOWithHessian;
        AFTOWithHessian func = (AFTOWithHessian) f;

        GoldenSectionMethod gsm = new GoldenSectionMethod();
        Point point = initialPoint;

        while(true){
            Point gradient = func.getGradientAtPoint(point);
            double norm = gradient.norm();

            Matrix hessian = func.getHessianAtPoint(point);

            Point solution = Point.fromMatrix(LinAlgUtils.solveSystem(hessian,Matrix.fromVector(gradient), LinAlgUtils.Method.LUP));

            Point translation = solution.multiply(-1);

            if(norm<mEpsilon){
                break;
            }
            switch (mType){
                case BY_GRAD_LENGTH:
                    point = point.plus(translation);
                    break;
                case OPTIMIZATION_ON_LINE:
                    final Point x0 = point;
                    gsm.findMinimum(new AbstractFunction() {
                        @Override
                        protected double internalValueAt(Point l) {
                            double lambda = l.get(0);
                            return func.valueAt(x0.plus(translation.multiply(lambda)));
                        }
                    }, Point.of(0));
                    break;
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
