package hr.fer.zemris.ropaeruj.dz8;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by ivan on 12/9/15.
 */
public class ANNTrainerTest {
    @Test
    public void testTDNN() throws Exception {
        ANNTrainer.main(new String[]{"data.txt", "tdnn-8x10x1", "15", "0.02", "50000"});
    }

    @Test
    public void testELMAN() throws Exception {
        ANNTrainer.main(new String[]{"data.txt", "elman-1x10x1", "10", "0.02", "5000"});
    }

    @Test
    public void testDataLoader() throws Exception {
        ParseableReadOnlyDataset dataset = ParseableReadOnlyDataset.loadData("data.txt", 4, -1);
        for (int i = 0; i < dataset.getSize(); i++) {
            System.out.println(Arrays.toString(dataset.getInputAt(i)) + " -> "+ Arrays.toString(dataset.getOutputAt(i)));
        }
    }
}