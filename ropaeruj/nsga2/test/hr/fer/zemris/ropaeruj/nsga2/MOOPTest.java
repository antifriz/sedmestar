package hr.fer.zemris.ropaeruj.nsga2;

import org.junit.Test;

/**
 * Created by ivan on 12/18/15.
 */
public class MOOPTest {

    @Test
    public void testMain() throws Exception {
        MOOP.main("2 1000 100".split(" "));
    }
}