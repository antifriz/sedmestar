package hr.fer.zemris.apr.dz2;

/**
 * Created by ivan on 11/8/15.
 */
public class Config {
    public static double PRECISION_9 = Math.pow(10, -9);
    public static double PRECISION_6 = Math.pow(10, -6);
    public static double PRECISION_3 = Math.pow(10, -3);

    public static IOptimizingMethod[] getMethods(boolean withGoldenSection) {
        if (withGoldenSection) {
            return new IOptimizingMethod[]{new GoldenSectionMethod(), new NelderMeadSimplex(), new HookeJevesMethod()};
        } else {
            return new IOptimizingMethod[]{new NelderMeadSimplex(), new HookeJevesMethod()};
        }
    }
}
