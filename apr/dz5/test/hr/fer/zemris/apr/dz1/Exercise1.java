package hr.fer.zemris.apr.dz1;

import hr.fer.zemris.apr.dz5.DiffMethod;
import hr.fer.zemris.apr.dz5.RungeKutta;
import org.junit.Test;

/**
 * Created by ivan on 1/17/16.
 */
public class Exercise1 {
    @Test
    public void testRungeKuta() throws Exception {
        Matrix A = Matrix.load("A1.txt");
        Matrix B = Matrix.load("B1.txt");
        Matrix x0 = Matrix.load("x01.txt");

        System.out.println(A);
        System.out.println(B);
        System.out.println(x0);

        DiffMethod.createRungeKutta(A, B, x0, 0.1, 10, 1).run();
    }
    @Test
    public void testTrapezoidal() throws Exception {
        Matrix A = Matrix.load("A1.txt");
        Matrix B = Matrix.load("B1.txt");
        Matrix x0 = Matrix.load("x01.txt");

        System.out.println(A);
        System.out.println(B);
        System.out.println(x0);

        DiffMethod.createTrapezoidalMethod(A, B, x0, 0.1, 10, 1).run();
    }
}
