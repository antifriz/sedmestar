package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/8/15.
 */
public interface IFunction {
    double valueAt(Point point);

    default int dimension(int desired){
        return desired;
    }
}
