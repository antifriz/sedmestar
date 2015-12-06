package hr.fer.zemris.apr.dz3;

/**
 * Created by ivan on 12/6/15.
 */
public class GradientDescent implements IOptimizingMethod {



    enum Type{
        BY_GRAD_LENGTH,
        OPTIMIZATION_ON_LINE
    }

    private Type mType;
    private double mEpsilon;

    public GradientDescent( Type type,double epsilon) {
        mType = type;
        mEpsilon = epsilon;
    }

    @Override
    public Point findMinimum(AbstractFunction f, Point initialPoint) {
        assert f instanceof AFTOWithDerivatives;
        AFTOWithDerivatives func = (AFTOWithDerivatives) f;

        GoldenSectionMethod gsm = new GoldenSectionMethod();
        Point point = initialPoint;

        while(true){
            Point gradient = func.getGradientAtPoint(point);
            double norm = gradient.norm();
            if(norm<mEpsilon){
                break;
            }
            switch (mType){
                case BY_GRAD_LENGTH:
                    point = point.minus(gradient);
                    break;
                case OPTIMIZATION_ON_LINE:
                    final Point x0 = point;
                    gsm.findMinimum(new AbstractFunction() {
                        @Override
                        protected double internalValueAt(Point l) {
                            double lambda = l.get(0);
                            return func.valueAt(x0.minus(gradient.multiply(lambda)));
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
