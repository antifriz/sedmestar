package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.Matrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 12/6/15.
 */
public class ProxyFunctionWIthDerivatives extends AFTOWithDerivatives{
    private AFTOWithDerivatives internalFunction;

    private Map<Point, Double> cacheVal = new HashMap<>();
    private Map<Point, Point> cacheGrad = new HashMap<>();
    private Map<Point, Matrix> cacheHess = new HashMap<>();

    public ProxyFunctionWIthDerivatives(AFTOWithDerivatives internalFunction) {
        this.internalFunction = internalFunction;

    }

    @Override
    protected double internalValueAt(Point point) {
        return cacheVal.computeIfAbsent(point, point1 -> internalFunction.valueAt(point));
    }

    @Override
    protected Point internalGradientAt(Point point) {
        return cacheGrad.computeIfAbsent(point, point1 -> internalFunction.gradientAt(point));
    }

    @Override
    protected Matrix internalHessianAt(Point point) {
        return cacheHess.computeIfAbsent(point, point1 -> internalFunction.hessianAt(point));
    }

    @Override
    public Point startingPoint(int n) {
        return null;
    }
}
