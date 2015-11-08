package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/7/15.
 */
public interface IFunctionToOptimize extends IFunction {

    Point startingPoint(int n);

    default Point minimumAt(int n) {
        return Point.zeros(n);
    }

    default double minimumValue() {
        return 0;
    }

}
