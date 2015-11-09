package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/8/15.
 */
public abstract class AbstractFunction {
    int callCount = 0;

    final double valueAt(Point point){
        callCount++;
        return internalValueAt( point);
    }

    protected abstract double internalValueAt(Point point);

    int dimension(int desired){
        return desired;
    }
}
