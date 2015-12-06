package hr.fer.zemris.apr.dz2;

import hr.fer.zemris.apr.dz3.AbstractFunction;
import hr.fer.zemris.apr.dz3.GoldenSectionMethod;
import hr.fer.zemris.apr.dz3.Point;
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

        for (double h = 1; h < 20; h++) {
            for (int point = -20; point < 20; point++) {
                double optima = goldenSectionMethod.findOptima(new AbstractFunction() {
                    @Override
                    protected double internalValueAt(Point point) {
                        return Math.pow(point.get(0) - 3,2);
                    }
                }, point, false);
                goldenSectionMethod.h = h;
                assertEquals(optima,3,Math.pow(10,6));
            }
        }
    }
}
