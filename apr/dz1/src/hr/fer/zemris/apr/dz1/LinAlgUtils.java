package hr.fer.zemris.apr.dz1;

import javafx.util.Pair;

/**
 * Created by ivan on 10/21/15.
 */
public class LinAlgUtils {
    public enum Method {
        LU,
        LUP
    }

    public static Matrix solveSystem(Matrix system, Matrix rhs, Method method) throws ArithmeticException {
        Matrix matrix;
        Matrix transform;

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

        Matrix interResult = l.supstituteForward(transform != null ? transform.times(rhs) : rhs);

        return u.supstituteBackward(interResult);
    }
}
