package hr.fer.zemris.apr.dz3;

/**
 * Created by ivan on 11/7/15.
 */
public abstract class AbstractFunctionToOptimize extends AbstractFunction {

    public abstract Point startingPoint(int n);

    public Point minimumAt(int n) {
        return Point.zeros(n);
    }

    public double minimumValue() {
        return 0;
    }

}
