package hr.fer.zemris.ropaeruj.nsga;

import org.junit.Test;

/**
 * Created by ivan on 12/18/15.
 */
public class MOOPTest {

    @Test
    public void testMain() throws Exception {
        MOOP.main("2 1000 decision-space 1000 0.1 0.1".split(" "));
    }
}