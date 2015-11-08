package hr.fer.zemris.apr.dz2;

import hr.fer.zemris.apr.dz2.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ivan on 11/8/15.
 */
@RunWith(Parameterized.class)
public class Exercise1 {

    private final int initial;

    public Exercise1(Integer initial) {
        this.initial = initial;
    }

    @Parameterized.Parameters(name = "x0 = {0}")
    public static Collection params() {
        List<Object[]> objects = new ArrayList<>();

        for (int i = 10; i < 10000; i *= 2) {
            objects.add(new Object[]{i});
        }
        return objects;
    }

    @Test
    public void testGoldenWithUnimodal() {

        GoldenSectionMethod goldenSectionMethod = new GoldenSectionMethod();

        IFunction1D function1d = x -> (x - 3) * (x - 3);

        double optima = goldenSectionMethod.findOptima(function1d, 1, initial, false);
        assertEquals(optima, 3, Config.PRECISION_6);
    }

    @Test
    public void testNMS(){
        IFunction1D function1d = x -> (x - 3) * (x - 3);
        IFunction function = point -> function1d.valueAt(point.get(0));

        NelderMeadSimplex simplex = new NelderMeadSimplex();
        assertEquals(simplex.findMinimum(function, Point.of(initial)).get(0), 3, Config.PRECISION_3);
    }
    @Test
    public void testHJM(){
        IFunction1D function1d = x -> (x - 3) * (x - 3);
        IFunction function = point -> function1d.valueAt(point.get(0));

        HookeJevesMethod hooke = new HookeJevesMethod();
        hooke.findMinimum(function, Point.of(initial));
        assertEquals(hooke.findMinimum(function, Point.of(initial)).get(0), 3, Config.PRECISION_3);
    }
}
