package hr.fer.zemris.ropaeruj.evo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ivan on 10/31/15.
 */
public class GeneticAlgorithmTest {

    @Test
    public void testMain() {
            GeneticAlgorithm.main("dataset2.txt 100 0 10000 tournament:10 1".split(" "));

    }
}