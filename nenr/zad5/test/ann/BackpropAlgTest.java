package ann;

import org.junit.Test;

import java.util.*;

/**
 * Created by ivan on 12/14/15.
 */
public class BackpropAlgTest {

    @Test
    public void testRun() throws Exception {
        final List<double[][]> rawDataset = new ArrayList<>();
        rawDataset.add(new double[][]{{0,0},{0}});
        rawDataset.add(new double[][]{{1,1},{1}});
        //rawDataset.add(new double[][]{{0,1},{1}});
        //rawDataset.add(new double[][]{{1,0},{1}});

        IReadOnlyDataset dataset = new IReadOnlyDataset() {
            List<double[][]> mDataset = rawDataset;

            @Override
            public int getInputDimension() {
                return mDataset.get(0)[0].length;
            }

            @Override
            public int getOutputDimension() {
                return mDataset.get(0)[1].length;
            }

            @Override
            public void reset() {

            }

            @Override
            public void next() {

            }

            @Override
            public List<double[][]> getWhole() {
                return Collections.unmodifiableList(mDataset);
            }

            @Override
            public int getSize() {
                return mDataset.size();
            }

            @Override
            public Iterator<double[][]> iterator() {
                return mDataset.iterator();
            }
        };
        FFANN ffann = FFANN.create(new int[]{dataset.getInputDimension(), dataset.getOutputDimension()}, new SigmoidTransferFunction());
        BackpropAlg backpropAlg = new BackpropAlg(ffann, dataset);
        backpropAlg.run(0.000000, 10000000);
    }
}