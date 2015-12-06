package hr.fer.zemris.apr.dz3;

import org.junit.Test;

/**
 * Created by ivan on 12/6/15.
 */

public class Exercise2 {
    @Test
    public void testByOptOnLine() throws Exception {
        IOptimizingMethod optimizingMethod = new DerivationMethod(DerivationMethod.Method.NEWTON_RAPHSON, DerivationMethod.Type.OPTIMIZATION_ON_LINE);

        AFTOWithDerivatives f1 = Functions.get(1);

        Point minimum = optimizingMethod.findMinimum(f1, f1.startingPoint(f1.dimension(2)));

        System.out.println(minimum);
        System.out.println(f1.minimumAt(f1.dimension(2)));

    }
}
