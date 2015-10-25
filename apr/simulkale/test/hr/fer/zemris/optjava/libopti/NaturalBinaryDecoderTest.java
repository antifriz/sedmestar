package hr.fer.zemris.optjava.libopti;

import hr.fer.zemris.optjava.dz3.RegresijaSustava;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by ivan on 10/23/15.
 */
public class NaturalBinaryDecoderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testBinary10() throws Exception {
        run("binary:10");
    }

    @Test
    public void testBinary5() throws Exception {
        run("binary:5");
    }

    @Test
    public void testBinary20() throws Exception {
        run("binary:20");
    }

    @Test
    public void testDecimal() throws Exception {
        run("decimal");
    }

    private void run(String s) {
        RegresijaSustava.main(new String[]{"02-zad-prijenosna.txt", s});
    }


    public void testBitvectorDecoders() throws Exception {
        BitvectorDecoder grayBinaryDecoder = new GreyBinaryDecoder(new double[]{0, 0}, new double[]{1, 1}, new int[]{10, 10}, 2);
        BitvectorSolution object = BitvectorSolution.create(grayBinaryDecoder.mTotalBits);
        object.mBytes[0] = -1;
        System.out.println(Arrays.toString(grayBinaryDecoder.decode(object)));


    }
}
