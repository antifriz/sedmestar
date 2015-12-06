package hr.fer.zemris.apr.dz2;

import hr.fer.zemris.apr.dz3.*;
import org.junit.Test;

/**
 * Created by ivan on 11/8/15.
 */
public class Exercise2 {

    @Test
    public void test() {
        for (int i = 0; i < 4; i++) {
            AbstractFunctionToOptimize function = Functions.get(i);
            int dimension = function.dimension(5);
            Point startingPoint = function.startingPoint(dimension);


            NelderMeadSimplex simplex = new NelderMeadSimplex();
            Point ps = simplex.findMinimum(function, startingPoint);
            int ccs = function.getAfterOptimizationCallCount();


            HookeJevesMethod hooke = new HookeJevesMethod();
            Point ph = hooke.findMinimum(function, startingPoint);
            int cch = function.getAfterOptimizationCallCount();


            System.out.printf("Function %d, dim=%d || NMS: calls=%5d dev=%6.4f %s | HJM: calls=%5d dev=%6.4f %s\n",i+1,dimension, ccs, PointUtils.deviation(ps, function.minimumAt(dimension)),ps, cch, PointUtils.deviation(ph, function.minimumAt(dimension)),ph);

        }
    }
}
