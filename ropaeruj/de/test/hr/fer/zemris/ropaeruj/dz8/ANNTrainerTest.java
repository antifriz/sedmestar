package hr.fer.zemris.ropaeruj.dz8;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by ivan on 12/9/15.
 */
public class ANNTrainerTest {
    @Test
    public void testTDNN() throws Exception {
        ANNTrainer.main(new String[]{"data.txt", "tdnn-8x10x1", "15", "0.01", "10000"});
    }

    @Test
    public void testELMAN() throws Exception {
        ANNTrainer.main(new String[]{"data.txt", "elman-1x10x1", "10", "0.02", "5000"});
    }
}