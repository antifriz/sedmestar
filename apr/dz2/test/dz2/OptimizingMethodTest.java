package dz2;

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
public class OptimizingMethodTest {

    int dimension;
    double precision = 1;
    IFunctionToOptimize function;
    IOptimizingMethod method;

    public OptimizingMethodTest(String optimizingMethodName, int id, Integer dimension, IFunctionToOptimize function, IOptimizingMethod method) {
        this.function = function;
        this.dimension = dimension;
        this.method = method;
    }


    @Parameterized.Parameters(name = "{0} func:{1} dim:{2}")
    public static Collection params() {
        List<Object[]> objects = new ArrayList<>();

        int[] dims = new int[]{1, 2, 3, 5, 10};
        IOptimizingMethod[] methods = new IOptimizingMethod[]{new NelderMeadSimplex(), new HookeJevesMethod()};
        for (IOptimizingMethod method : methods) {
            for (int i = 0; i < Functions.size(); i++) {
                for (int d : dims) {
                    IFunctionToOptimize iFunctionToOptimize = Functions.get(i);
                    if (iFunctionToOptimize.dimension(d) == d) {
                        objects.add(new Object[]{method.getClass().getSimpleName(), i, d, iFunctionToOptimize, method});
                    }
                }
            }
        }
        return objects;
    }

    @Test
    public void testOptimization() {
        Point p = method.findMinimum(function, function.startingPoint(function.dimension(dimension)));

        assertEquals(function.minimumValue(), function.valueAt(p), precision);
    }

}
