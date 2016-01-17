package hr.fer.zemris.apr.dz1;

import org.junit.Test;

/**
 * Created by ivan on 1/17/16.
 */

public class Tester {
    @Test
    public void testInverse() {
        Matrix matrix = Matrix.createIdentity(4);
        System.out.println(matrix);
        System.out.println(matrix.inverse());
    }
}
