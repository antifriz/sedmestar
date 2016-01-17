package hr.fer.zemris.optjava.rng;

import ec.util.MersenneTwisterFast;
import hr.fer.zemris.optjava.rng.ga.EvoThread;
import org.junit.Test;

import java.util.Random;

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
        EvoThread thread = new EvoThread(() -> {
            IRNG rng = RNG.getRNG();
            for (int i = 0; i < 20; i++) {
                System.out.println(rng.nextInt(-5, 5));
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testCompareRNGS() throws Exception {
        MersenneTwisterFast msf = new MersenneTwisterFast(69);
        long currentTime = System.currentTimeMillis();
        int times = 100_000_000;
        for (int i = 0; i < times; i++) {
            msf.nextLong();
        }
        System.out.println("msf "+(System.currentTimeMillis()-currentTime));

        Random random = new Random(69);
        long currentTime2 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            random.nextLong();
        }
        System.out.println("javica "+(System.currentTimeMillis()-currentTime2));

        long currentTime3 = System.currentTimeMillis();
        long x = currentTime3;
        for (int i = 0; i < times; i++) {
            x ^= (x << 21);
            x ^= (x >>> 35);
            x ^= (x << 4);
        }
        System.out.println("xor "+(System.currentTimeMillis()-currentTime3));

    }
}
