package hr.fer.zemris.apr.dz2;

import hr.fer.zemris.apr.dz2.GoldenSectionMethod;
import hr.fer.zemris.apr.dz2.IFunction1D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ivan on 11/7/15.
 */
public class GoldenSectionMethodTest {
    @Test
    public void testGoldenWithUnimodal() {

        GoldenSectionMethod goldenSectionMethod = new GoldenSectionMethod();

        IFunction1D function = x -> (x - 3) * (x - 3);

        for (double h = 1; h < 20; h++) {
            for (int point = -20; point < 20; point++) {
                double optima = goldenSectionMethod.findOptima(function, h, point, false);
                assertEquals(optima,3,Math.pow(10,6));
            }
        }
    }
}
