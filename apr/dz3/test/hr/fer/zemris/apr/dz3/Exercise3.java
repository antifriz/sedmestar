package hr.fer.zemris.apr.dz3;

import org.junit.Test;

/**
 * Created by ivan on 12/6/15.
 */

public class Exercise3 {
    @Test
    public void testBox1() throws Exception {
        innerTest(0, true);
    }

    @Test
    public void testBox1WOImplicit() throws Exception {
        innerTest(0, false);
    }

    @Test
    public void testBox2() throws Exception {
        innerTest(1, true);
    }

    @Test
    public void testBox2WOImplicit() throws Exception {
        innerTest(1, false);
    }

    private void innerTest(int functionId, boolean useImplicit) {
        IOptimizingMethod method = new BoxMethod(-100, 100, point -> !useImplicit || (point.get(1) >= point.get(0) && 2 >= point.get(0)));

        AFTOWithDerivatives f = Functions.get(functionId);
        ProxyFunctionWithDerivatives proxy = new ProxyFunctionWithDerivatives(f);

        Point minimum = method.findMinimum(proxy, proxy.startingPoint(proxy.dimension(2)));

        System.out.println("Found minimum:");
        System.out.println(minimum);
        System.out.println("Real minimum:");
        System.out.println(proxy.minimumAt(proxy.dimension(2)));

        System.out.println(proxy.getStats());
    }
}
