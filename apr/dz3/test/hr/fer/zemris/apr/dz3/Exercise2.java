package hr.fer.zemris.apr.dz3;

import org.junit.Test;

/**
 * Created by ivan on 12/6/15.
 */

public class Exercise2 {
    @Test
    public void testGrad1() throws Exception {
        innerTest(DerivationMethod.Method.GRADIENT_DESCENT,0);
    }
    @Test
    public void testNR1() throws Exception {
        innerTest(DerivationMethod.Method.NEWTON_RAPHSON,0);
    }
    @Test
    public void testGrad2() throws Exception {
        innerTest(DerivationMethod.Method.GRADIENT_DESCENT,1);
    }
    @Test
    public void testNR2() throws Exception {
        innerTest(DerivationMethod.Method.NEWTON_RAPHSON,1);
    }

    private void innerTest(DerivationMethod.Method dMethod, int functionId) {
        IOptimizingMethod method = new DerivationMethod(dMethod, DerivationMethod.Type.OPTIMIZATION_ON_LINE);

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
