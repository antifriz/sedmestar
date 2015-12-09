package hr.fer.zemris.ropaeruj.dz8;

import org.junit.Test;

/**
 * Created by ivan on 12/9/15.
 */
public class ANNTrainerTest {
    @Test
    public void testMain() throws Exception {
        ANNTrainer.main(new String[]{"data.txt", "tdnn-8x10x1", "10", "0.1", "1000"});
    }
}
