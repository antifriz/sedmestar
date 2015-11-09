package hr.fer.zemris.apr.dz2;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ivan on 11/8/15.
 */
public class Exercise5 {

    @Test
    public void test() {
        int counter = 0;
        int n = 10;
        for (int i = 0; i < n; i++) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Point of = Point.of(random.nextDouble(-50, 50), random.nextDouble(-50, 50));
            if (run(of)) {
                counter++;
            }
        }
        System.out.printf("%4.2f%%\n", counter / (double) n * 100);
    }

    private boolean run(Point startingPoint) {
        AbstractFunctionToOptimize function = Functions.get(4);

        NelderMeadSimplex simplex = new NelderMeadSimplex();
        simplex.setVerbosity(false);
        simplex.setTimeout(1000);
        simplex.simplexT = 50;
        Point ps;
        try {
            ps = simplex.findMinimum(function, startingPoint);
            // int ccs = function.getAfterOptimizationCallCount();
            //int dimension = startingPoint.getDimension();
            //System.out.printf("NMS: %5d %s %6.4f\n", ccs, ps, PointUtils.deviation(ps, function.minimumAt(dimension)));
            return Math.abs(function.minimumValue() - function.valueAt(ps)) <= 0.0001;
        } catch (Throwable e) {
            return false;
        }
    }

}
