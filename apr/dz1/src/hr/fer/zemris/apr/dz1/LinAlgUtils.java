package hr.fer.zemris.apr.dz1;

import javafx.util.Pair;

/**
 * Created by ivan on 10/21/15.
 */
public class LinAlgUtils {
    public static Matrix solveSystem(Matrix system, Matrix rhs, Method method) throws ArithmeticException {
        Matrix matrix;
        Matrix transform;

        //System.out.println("System:");
        //System.out.println(system);
        //System.out.println("Rhs:");
        //System.out.println(rhs);

        if (method == Method.LUP) {
            Pair<Matrix, Matrix> pair = system.decomposeLUP();
            matrix = pair.getKey();
            transform = pair.getValue();
        } else {
            matrix = system.decomposeLU();
            transform = null;
        }

        Matrix u = matrix.extractUpper();
        Matrix l = matrix.extractLower();

        //System.out.println("Lower:");
        //System.out.println(l);

        Matrix rhsVector = transform != null ? transform.times(rhs) : rhs;
        //System.out.println("Rhs:");
        //System.out.println(rhsVector);

        Matrix interResult = l.supstituteForward(rhsVector);

        //System.out.println("Inner result:");
        //System.out.println(interResult);

        //System.out.println("Upper:");
        //System.out.println(u);

        return u.supstituteBackward(interResult);
    }

    public enum Method {
        LU,
        LUP
    }
}
