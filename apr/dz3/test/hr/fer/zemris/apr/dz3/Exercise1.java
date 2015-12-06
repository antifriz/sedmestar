package hr.fer.zemris.apr.dz3;

import org.junit.Test;

/**
 * Created by ivan on 12/6/15.
 */

public class Exercise1 {
    @Test
    public void testByOptOnLine() throws Exception {
        IOptimizingMethod method = new DerivationMethod(DerivationMethod.Method.GRADIENT_DESCENT,DerivationMethod.Type.OPTIMIZATION_ON_LINE);

        AFTOWithDerivatives f1 = Functions.get(0);

        Point minimum = method.findMinimum(f1, f1.startingPoint(f1.dimension(2)));

        System.out.println(minimum);
        System.out.println(f1.minimumAt(f1.dimension(2)));

    }
}
