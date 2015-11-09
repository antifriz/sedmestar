package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/7/15.
 */
public abstract class AbstractFunction1D {
    int callCount = 0;

    abstract double internalValueAt(double domainValue);

    double valueAt(double domainValue) {
        callCount++;
        return internalValueAt(domainValue);
    }
}
