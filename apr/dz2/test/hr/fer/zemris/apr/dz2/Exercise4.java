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
        AbstractFunctionToOptimize function = Functions.get(3);

        NelderMeadSimplex simplex = new NelderMeadSimplex();
        simplex.setVerbosity(true);
        simplex.simplexT=simplexStep;
        simplex.setTimeout( 1000);
        Point ps = simplex.findMinimum(function, startingPoint);
        int ccs = function.getAfterOptimizationCallCount();


        int dimension = startingPoint.getDimension();
        System.out.printf("NMS, Calls: %5d Point: %s Deviation: %6.4f\n", ccs, ps, PointUtils.deviation(ps, function.minimumAt(dimension)));
        org.junit.Assert.assertEquals(function.minimumValue(),function.valueAt(ps),Config.PRECISION_3);
    }

    @Test
    public void testB() {
        run(Point.of(20, 20));
    }
}
