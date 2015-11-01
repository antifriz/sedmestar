package hr.fer.zemris.optjava.dz4.part2;

import hr.fer.zemris.optjava.dz4.part1.GeneticAlgorithm;
import org.junit.Test;

/**
 * Created by ivan on 10/31/15.
 */
public class BoxFillingTest {

    @Test
    public void testMain() {
            BoxFilling.main("problem-20-10-1.dat 5 10 10 true 100 10".split(" "));
    }
}