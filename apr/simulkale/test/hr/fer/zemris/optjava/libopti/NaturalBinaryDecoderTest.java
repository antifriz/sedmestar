package hr.fer.zemris.optjava.libopti;

import hr.fer.zemris.optjava.dz3.RegresijaSustava;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by ivan on 10/23/15.
 */
public class NaturalBinaryDecoderTest {
    private Random mRandom;

    @Before
    public void setUp() throws Exception {
        mRandom = new Random(69);

    }

    @Test
    public void testInput() throws Exception {
        RegresijaSustava.main(new String[]{"02-zad-prijenosna.txt", "binary:10"});
    }


}
