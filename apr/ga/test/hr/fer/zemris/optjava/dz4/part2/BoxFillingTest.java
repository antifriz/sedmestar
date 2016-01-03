package hr.fer.zemris.optjava.dz4.part2;

import org.junit.Test;

/**
 * Created by ivan on 10/31/15.
 */
public class BoxFillingTest {

    @Test
    public void testMain() {
        for (int i = 10; i <= 50; i += 20) {
            for (int j = 1; j <= 5; j++) {
                BoxFilling.main(String.format("problem-20-%d-%d.dat 5 10 10 true 100 10", i, j).split(" "));
            }
        }
    }
}