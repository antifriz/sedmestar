package hr.fer.zemris.apr.dz2;

import org.junit.Test;

/**
 * Created by ivan on 11/8/15.
 */
public class Exercise2 {

    @Test
    public void test() {
        for (int i = 0; i < 4; i++) {
            IFunctionToOptimize function = Functions.get(i);
            int dimension = function.dimension(5);
            Point startingPoint = function.startingPoint(dimension);


            NelderMeadSimplex simplex = new NelderMeadSimplex();
            Point ps = simplex.findMinimum(function, startingPoint);


            HookeJevesMethod hooke = new HookeJevesMethod();
            Point ph = hooke.findMinimum(function, startingPoint);


            System.out.printf("Function %d, dim=%d || NMS: %5d %6.4f | HJM: %5d %6.4f\n",i+1,dimension, simplex.lastIterationCount, PointUtils.deviation(ps, function.minimumAt(dimension)), hooke.lastIterationCount, PointUtils.deviation(ph, function.minimumAt(dimension)));

        }
    }
}
