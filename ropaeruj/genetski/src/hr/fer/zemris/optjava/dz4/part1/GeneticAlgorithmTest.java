package hr.fer.zemris.optjava.dz4.part1;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ivan on 10/31/15.
 */
public class GeneticAlgorithmTest {

    @Test
    public void testMain() {
            GeneticAlgorithm.main("02-zad-prijenosna.txt 100 0 2000 tournament:10 1".split(" "));


    }
}