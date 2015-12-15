package ann;

import model.BatchReadOnlyDataset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivan on 12/14/15.
 */
public class BackpropAlgTest {

    @Test
    public void testQuadratic() throws Exception {
        final List<double[][]> rawDataset = new ArrayList<>();
        for (double i = -1; i <= 1; i += 0.2) {
            rawDataset.add(new double[][]{{i}, {i * i}});
        }

        IReadOnlyDataset dataset = new BatchReadOnlyDataset(rawDataset,1);

        FFANN ffann = FFANN.create(new int[]{dataset.getInputDimension(), 6, dataset.getOutputDimension()}, new SigmoidTransferFunction());
        BackpropAlg backpropAlg = new BackpropAlg(ffann, dataset);
        double[] weights = backpropAlg.run(0, 1000);
        System.out.println(Arrays.toString(weights));
        double[] out = new double[1];
        for (double i = -1; i <= 1; i += 0.1) {
            ffann.calcOutputs(new double[]{i}, weights, out);
            System.out.println(Arrays.toString(out));
        }
    }

}