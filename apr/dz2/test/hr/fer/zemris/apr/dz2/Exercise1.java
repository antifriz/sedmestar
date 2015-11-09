package hr.fer.zemris.apr.dz2;

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
    private AbstractFunction1D mFunction1d = new AbstractFunction1D() {
        @Override
        double internalValueAt(double x) {
            return (x - 3) * (x - 3);
        }
    };

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

        double optima = goldenSectionMethod.findOptima(mFunction1d, 1, initial, false);
        System.out.println(mFunction1d.callCount);
        assertEquals(optima, 3, Config.PRECISION_6);
    }

    @Test
    public void testNMS() {
        NelderMeadSimplex simplex = new NelderMeadSimplex();
        //simplex.verbose= true;
        runMulti(simplex);
    }

    @Test
    public void testHJM() {
        HookeJevesMethod hooke = new HookeJevesMethod();
//        hooke.verbose= true;

        runMulti(hooke);
    }

    private void runMulti(IOptimizingMethod method) {
        AbstractFunction function = new AbstractFunction() {
            @Override
            protected double internalValueAt(Point point) {
                return mFunction1d.valueAt(point.get(0));
            }
        };

        Point minimum = method.findMinimum(function, Point.of(initial));

        System.out.println(function.callCount);

        assertEquals(minimum.get(0), 3, Config.PRECISION_6);
    }

}
