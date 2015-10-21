package hr.fer.zemris.apr.dz1;

/**
 * Created by ivan on 10/21/15.
 */
public class LinAlgUtils {
    enum Method {
        LU,
        LUP
    }

    public Matrix solveSystem(Matrix system, Matrix rhs, Method method) {
        Matrix matrix = method == Method.LU ? system.decomposeLU() : system.decomposeLUP();
        Matrix u = matrix.extractUpper();
        Matrix newRhs = u.supstituteForward(rhs);
        Matrix l = matrix.extractLower();
        return l.supstituteBackward(newRhs);
    }
}
