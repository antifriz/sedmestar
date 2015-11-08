package hr.fer.zemris.apr.dz2;

import org.junit.Test;

/**
 * Created by ivan on 11/8/15.
 */
public class Exercise3 {

    @Test
    public void test() {
        IFunctionToOptimize function = Functions.get(3);
        Point startingPoint = Point.of(5, 5);

        NelderMeadSimplex simplex = new NelderMeadSimplex();
        Point ps = simplex.findMinimum(function, startingPoint);


        HookeJevesMethod hooke = new HookeJevesMethod();
        Point ph = hooke.findMinimum(function, startingPoint);


        int dimension = startingPoint.getDimension();
        System.out.printf("NMS: %5d %s %6.4f | HJM: %5d %s %6.4f\n", simplex.lastIterationCount, ps, PointUtils.deviation(ps, function.minimumAt(dimension)), hooke.lastIterationCount, ph, PointUtils.deviation(ph, function.minimumAt(dimension)));
    }
}
