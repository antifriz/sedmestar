package hr.fer.zemris.apr.dz3;

/**
 * Created by ivan on 11/7/15.
 */
public abstract class AbstractFunctionToOptimize extends AbstractFunction {

    public abstract Point startingPoint(int n);

    public abstract Point minimumAt(int n);

    public abstract double minimumValue();

}
