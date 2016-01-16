package hr.fer.zemris.optjava.rng;

import org.junit.Test;

/**
 * Created by ivan on 1/16/16.
 */
public class TestRNG {
    @Test
    public void testRNG() throws Exception {
        IRNG rng = RNG.getRNG();
        for (int i = 0; i < 20; i++) {
            System.out.println(rng.nextInt(-5, 5));
        }
    }
    @Test
    public void testEvoThread() throws Exception {
        EVOThread thread = new EVOThread(() -> {
            IRNG rng = RNG.getRNG();
            for (int i = 0; i < 20; i++) {
                System.out.println(rng.nextInt(-5, 5));
            }
        });
        thread.start();
        thread.join();
    }

}
