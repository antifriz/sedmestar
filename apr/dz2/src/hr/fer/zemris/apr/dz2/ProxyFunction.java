package hr.fer.zemris.apr.dz2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 11/9/15.
 */
public class ProxyFunction extends AbstractFunction {
    private AbstractFunction internalFunction;

    private Map<Point, Double> cache = new HashMap<>();

    public ProxyFunction(AbstractFunction internalFunction) {
        this.internalFunction = internalFunction;

    }

    @Override
    protected double internalValueAt(Point point) {
        return cache.computeIfAbsent(point, point1 -> internalFunction.valueAt(point));
    }
}
