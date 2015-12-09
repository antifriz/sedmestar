package hr.fer.zemris.ropaeruj.dz8;

/**
 * Created by ivan on 11/16/15.
 */
public class TanhSigmoidTransferFunction implements ITransferFunction {
    @Override
    public double valueAt(double x) {
        return 2.0/(1.0+Math.exp(-x))-1;
    }
}
