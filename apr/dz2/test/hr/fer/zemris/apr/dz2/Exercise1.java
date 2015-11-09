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

    private final IOptimizingMethod method;
    private final int initial;

    public Exercise1(String methodName, IOptimizingMethod method, Integer initial) {
        this.method = method;
        this.initial = initial;
    }

    @Parameterized.Parameters(name = "{0}, x0 = {2}")
    public static Collection params() {
        List<Object[]> objects = new ArrayList<>();

        for (int i = 10; i < 10000; i *= 2) {
            for (IOptimizingMethod method : Config.getMethods(true)) {
                if(method instanceof NelderMeadSimplex){
                    ((NelderMeadSimplex) method).useTweak = false;
                }
                method.setVerbosity(true);
                objects.add(new Object[]{method.getClass().getSimpleName(), method, i});
            }
        }
        return objects;
    }


    @Test
    public void test() {
        AbstractFunction function = new AbstractFunction() {
            @Override
            protected double internalValueAt(Point point) {
                return Math.pow(point.get(0) - 3, 2);
            }
        };

        Point minimum = method.findMinimum(function, Point.of(initial));

        System.out.println("Calls: "+function.getAfterOptimizationCallCount());

        assertEquals(3, minimum.get(0), Config.PRECISION_3);
    }

}
