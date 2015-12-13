package ann;

/**
 * Created by ivan on 11/16/15.
 */
public class SigmoidTransferFunction implements ITransferFunction {
    @Override
    public double valueAt(double x) {
        return 1.0/(1.0+Math.exp(-x));
    }
}
