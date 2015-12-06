package hr.fer.zemris.apr.dz3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Created by ivan on 12/6/15.
 */
public class Exercise1 {
    @Test
    public void testOptOnLine() {
        innerTest(DerivationMethod.Type.OPTIMIZATION_ON_LINE);
    }
    @Test
    public void testByGradLength() {
        innerTest(DerivationMethod.Type.BY_GRAD_LENGTH);
    }

    private void innerTest(DerivationMethod.Type type) {
        IOptimizingMethod method = new DerivationMethod(DerivationMethod.Method.GRADIENT_DESCENT,type);

        AFTOWithDerivatives f1 = Functions.get(2);

        Point minimum = method.findMinimum(f1, f1.startingPoint(f1.dimension(2)));

        System.out.println("Found minimum:");
        System.out.println(minimum);
        System.out.println("Real minimum:");
        System.out.println(f1.minimumAt(f1.dimension(2)));
    }
}
