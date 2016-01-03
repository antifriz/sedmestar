package hr.fer.zemris.apr.ga;

/**
 * Created by ivan on 11/7/15.
 */
public abstract class AbstractFunctionToOptimize extends AbstractFunction {

    abstract Point startingPoint(int n);

    Point minimumAt(int n) {
        return Point.zeros(n);
    }

    double minimumValue() {
        return 0;
    }

}
