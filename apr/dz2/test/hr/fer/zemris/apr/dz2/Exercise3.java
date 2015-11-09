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
public class Exercise3 {


    private final IOptimizingMethod method;

    public Exercise3(String methodName, IOptimizingMethod method) {
        this.method = method;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection params() {
        List<Object[]> objects = new ArrayList<>();

        for (IOptimizingMethod method : Config.getMethods(false)) {
            objects.add(new Object[]{method.getClass().getSimpleName(), method});
        }
        return objects;
    }


    @Test
    public void test() {
        AbstractFunctionToOptimize function = Functions.get(3);
        Point startingPoint = Point.of(5, 5);
        method.setVerbosity(true);
        Point ps = method.findMinimum(function, startingPoint);
        int cc = function.getAfterOptimizationCallCount();
        int dimension = startingPoint.getDimension();
        System.out.printf("Calls: %5d Deviation: %6.4f \n", cc, PointUtils.deviation(ps, function.minimumAt(dimension)));
    }
}
