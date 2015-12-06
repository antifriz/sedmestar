package hr.fer.zemris.apr.dz3;

import org.junit.Test;

import java.util.function.Function;

/**
 * Created by ivan on 12/6/15.
 */
public class Exercise4 {
    @Test
    public void test1() throws Exception {
        innerTest(0);
    }
    @Test
    public void test2() throws Exception {
        innerTest(1);
    }

    private void innerTest(int functionId) {
//        IOptimizingMethod method = new BoxMethod(-100, 100, point -> !useImplicit || (point.get(1) >= point.get(0) && 2 >= point.get(0)));
        IOptimizingMethod method = new NelderMeadSimplex();


        AbstractFunctionToOptimize f = Functions.get(functionId);


        final double r = 0.001;
        AbstractFunction fLimits = new AbstractFunction() {
            @Override
            protected double internalValueAt(Point point) {
                double g1 = point.get(1) - point.get(0);
                double g2 = 2 - point.get(0);
                Function<Double, Double> extF = x -> 1 / 2.0 * Math.pow(x - Math.abs(x), 2);

                double internal = point.unaryOperation(x -> -r * Math.log(100 - x) - r * Math.log(x + 100)).sum();
                double external = 1 / r * (extF.apply(g1) + extF.apply(g2));
                return f.valueAt(point) + internal + external;
            }
        };
        //Point initialPoint = f.startingPoint(f.dimension(2));
        Point initialPoint = Point.of(0.5,1);
        Point minimum = method.findMinimum(fLimits, initialPoint);

        System.out.println("Found minimum:");
        System.out.println(minimum);
        System.out.println("Real minimum:");
        System.out.println(f.minimumAt(f.dimension(2)));

        System.out.println("Function calls: " + f.getAfterOptimizationCallCount());
    }
}
