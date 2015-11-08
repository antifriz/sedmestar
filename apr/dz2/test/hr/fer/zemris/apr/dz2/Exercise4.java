package hr.fer.zemris.apr.dz2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ivan on 11/8/15.
 */
@RunWith(Parameterized.class)
public class Exercise4 {

    private final int simplexStep;

    public Exercise4(Integer simplexStep) {
        this.simplexStep = simplexStep;
    }

    @Parameterized.Parameters(name = "x0 = {0}")
    public static Collection params() {
        List<Object[]> objects = new ArrayList<>();

        for (int i = 1; i <= 20; i ++) {
            objects.add(new Object[]{i});
        }
        return objects;
    }

    @Test
    public void testA() {
        run(Point.of(0.5, 0.5));
    }

    private void run(Point startingPoint) {
        IFunctionToOptimize function = Functions.get(3);

        NelderMeadSimplex simplex = new NelderMeadSimplex();
        simplex.verbose=false;
        simplex.simplexT=simplexStep;
        simplex.timeout = 1000;
        Point ps = simplex.findMinimum(function, startingPoint);


        int dimension = startingPoint.getDimension();
        System.out.printf("NMS: %5d %s %6.4f", simplex.lastIterationCount, ps, PointUtils.deviation(ps, function.minimumAt(dimension)));
        org.junit.Assert.assertEquals(function.minimumValue(),function.valueAt(ps),0.1);
    }

    @Test
    public void testB() {
        run(Point.of(20, 20));
    }
}
