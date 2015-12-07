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
        IOptimizingMethod method = new NelderMeadSimplex();
        AbstractFunctionToOptimize f = Functions.get(functionId);

        final double r = 1;
        AbstractFunction fLimits = new AbstractFunction() {
            @Override
            protected double internalValueAt(Point point) {
                double internal = point.unaryOperation(x -> ln(100 - x) + ln(x + 100)).sum() +ln(point.get(1) - point.get(0))+ln(2 - point.get(0));
                return f.valueAt(point) -r*internal;
            }
        };
        Point initialPoint = f.startingPoint(f.dimension(2));
        //initialPoint = Point.of(-1,0);
        //method.setVerbosity(true);
        Point minimum = method.findMinimum(fLimits, initialPoint);

        System.out.println("Found minimum:");
        System.out.println(minimum);
        System.out.println("Real minimum:");
        System.out.println(f.minimumAt(f.dimension(2)));

        System.out.println("Function calls: " + f.getAfterOptimizationCallCount());
    }

    private double ln(double x){
        return Math.log(Math.max(0,x));
    }
}
