package hr.fer.zemris.ropaeruj.dz8;

/**
 * Created by ivan on 11/16/15.
 */
public class SigmoidTransferFunction implements ITransferFunction {
    @Override
    public double valueAt(double x) {
        return 1.0/(1.0+Math.exp(-x));
    }
}
