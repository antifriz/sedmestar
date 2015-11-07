package hr.fer.zemris.optjava.dz5.part2;

import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivan on 11/7/15.
 */
public class GeneticAlgorithmTest {

    @Test
    public void testNug12() throws Exception {
        assertEquals(testFor("nug12.dat"), 578);
    }


    @Test
    public void testEls19() throws Exception {
        assertEquals(testFor("els19.dat"), 17212548);
    }

    @Test
    public void testNug25() throws Exception {
        assertEquals(testFor("nug25.dat"), 3744);
    }

    private int testFor(String path) {
        return GeneticAlgorithm.run("data/" + path);
    }
}