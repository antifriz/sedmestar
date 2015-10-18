package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by ivan on 10/18/15.
 */
public class MatrixUtils {
    public static void assertIs2DPoint(Matrix matrix) {
        assert matrix.getColumnDimension() == 1 && matrix.getRowDimension() == 2 : "Matrix must be a 2D vector";
    }

    public static void assertIsScalar(Matrix matrix) {
        assert matrix.getColumnDimension() == 1 && matrix.getRowDimension() == 1 : "Matrix must be a scalar";
    }

    public static double asScalar(Matrix matrix) {
        assertIsScalar(matrix);
        return matrix.get(0, 0);
    }

    public static String prettyPrintVector(Matrix currentOptimalPoint) {
        assertIsColumnVector(currentOptimalPoint);
        StringBuilder sb = new StringBuilder();
        double[] arr = currentOptimalPoint.getRowPackedCopy();
        DecimalFormat decimalFormat = new DecimalFormat(" 0.0000;-#");
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(decimalFormat.format(arr[i]));
            if (i + 1 != arr.length) {
                sb.append(";");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void assertIsColumnVector(Matrix matrix) {
        assert matrix.getColumnDimension() == 1 : "Matrix must be column vector";
    }
}
