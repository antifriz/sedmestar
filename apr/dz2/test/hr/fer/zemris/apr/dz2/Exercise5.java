package hr.fer.zemris.apr.dz2;

import org.junit.Test;

/**
 * Created by ivan on 11/8/15.
 */
public class Exercise5 {

    private final int simplexStep;

    public Exercise5(Integer simplexStep) {
        this.simplexStep = simplexStep;
    }

    @Test
    public void testA() {
        run(Point.of(0.5, 0.5));
    }

    private void run(Point startingPoint) {
        AbstractFunctionToOptimize function = Functions.get(5);

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
