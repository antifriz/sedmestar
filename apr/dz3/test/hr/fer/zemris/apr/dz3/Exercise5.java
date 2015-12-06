package hr.fer.zemris.apr.dz3;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by ivan on 12/6/15.
 */
public class Exercise5 {
    @Test
    public void test1() throws Exception {
        innerTest(0);
    }

    @Test
    public void test2() throws Exception {
        innerTest(1);
    }

    @Test
    public void testName() throws Exception {
        System.out.println(Math.log(0));

    }

    private void innerTest(int functionId) {
//        IOptimizingMethod method = new BoxMethod(-100, 100, point -> !useImplicit || (point.get(1) >= point.get(0) && 2 >= point.get(0)));
        IOptimizingMethod method = new NelderMeadSimplex();


        AbstractFunctionToOptimize f = Functions.get(functionId);

        List<Function<Point, Double>> outerLimits = new ArrayList<>();
        List<Function<Point, Double>> innerLimits = new ArrayList<>();

        innerLimits.add(point->3-point.get(0)-point.get(1));
        innerLimits.add(point->3+1.5*point.get(0)-point.get(1));

        outerLimits.add(point -> point.get(1)-1);

        Point initialPoint = f.startingPoint(f.dimension(2));
        double epsilon = Config.PRECISION_6;
        double tt = 1;
        while (true) {
            final double t = tt;
            AbstractFunction fLimits = new AbstractFunction() {
                @Override
                protected double internalValueAt(Point point) {
                    double value = f.valueAt(point);
                    double innerPart = innerLimits.stream().mapToDouble(limit -> limit.apply(point)).map(x -> -Math.log(Math.max(x,0))).sum();
                    double outerPart = outerLimits.stream().mapToDouble(limit -> limit.apply(point)).map(x -> x * x).sum();
                    return value + (1 / t) * innerPart + t * outerPart;
                }
            };
            Point minimum = method.findMinimum(fLimits, initialPoint);
            System.out.println(minimum);
            if(initialPoint.distanceTo(minimum)<epsilon){
                initialPoint = minimum;
                break;
            }
            System.out.println(initialPoint.distanceTo(minimum));
            initialPoint = minimum;
            tt *= 10;
        }

        System.out.println("Found minimum:");
        System.out.println(initialPoint);
        System.out.println("Real minimum:");
        System.out.println(f.minimumAt(f.dimension(2)));

        System.out.println("Function calls: " + f.getAfterOptimizationCallCount());
    }
}
