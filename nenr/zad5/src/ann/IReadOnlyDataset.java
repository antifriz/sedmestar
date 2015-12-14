package ann;

import java.util.List;

/**
 * Created by ivan on 11/16/15.
 */
public interface IReadOnlyDataset extends Iterable<double[][]>{
    int getInputDimension();

    int getOutputDimension();

    void reset();

    void next();

    List<double[][]> getWhole();

    int getSize();
}
