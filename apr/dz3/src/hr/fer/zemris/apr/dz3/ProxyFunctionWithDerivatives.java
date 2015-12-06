package hr.fer.zemris.apr.dz3;

import hr.fer.zemris.apr.dz1.Matrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 12/6/15.
 */
public class ProxyFunctionWithDerivatives extends AFTOWithDerivatives{
    private AFTOWithDerivatives internalFunction;

    private Map<Point, Double> cacheVal = new HashMap<>();
    private Map<Point, Point> cacheGrad = new HashMap<>();
    private Map<Point, Matrix> cacheHess = new HashMap<>();

    public ProxyFunctionWithDerivatives(AFTOWithDerivatives internalFunction) {
        this.internalFunction = internalFunction;
    }

    @Override
    protected double internalValueAt(Point point) {
        return cacheVal.computeIfAbsent(point, point1 -> internalFunction.valueAt(point1));
    }

    @Override
    protected Point internalGradientAt(Point point) {
        return cacheGrad.computeIfAbsent(point, point1 -> internalFunction.gradientAt(point1));
    }

    @Override
    protected Matrix internalHessianAt(Point point) {
        return cacheHess.computeIfAbsent(point, point1 -> internalFunction.hessianAt(point1));
    }

    @Override
    public Point startingPoint(int n) {
        return internalFunction.startingPoint(n);
    }

    @Override
    public Point minimumAt(int n) {
        return internalFunction.minimumAt(n);
    }

    @Override
    public double minimumValue() {
        return internalFunction.minimumValue();
    }

    public String getStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function calls: ").append(internalFunction.getAfterOptimizationCallCount()).append('\n');
        sb.append("Gradient calls: ").append(internalFunction.getAfterOptimizationGradientCallCOunt()).append('\n');
        sb.append("Hessian calls:  ").append(internalFunction.getAfterOptimizationHessianCallCount());
        return sb.toString();
    }
}
