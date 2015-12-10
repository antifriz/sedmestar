package hr.fer.zemris.ropaeruj.dz8;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by ivan on 12/9/15.
 */
public class ANNTrainerTest {
    @Test
    public void testMain() throws Exception {
        ANNTrainer.main(new String[]{"data.txt", "tdnn-8x10x4x1", "15", "0.02", "50000"});
    }

    @Test
    public void testDataLoader() throws Exception {
        ParseableReadOnlyDataset dataset = ParseableReadOnlyDataset.loadData("data.txt", 4, -1);
        for (int i = 0; i < dataset.getSize(); i++) {
            System.out.println(Arrays.toString(dataset.getInputAt(i)) + " -> "+ Arrays.toString(dataset.getOutputAt(i)));
        }
    }
}
