package hr.fer.zemris.apr.dz1;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by ivan on 10/19/15.
 */
public final class Matrix {
    private static final double COMPARE_THRESH = Math.pow(10, -9);
    private final double[][] mUnderlayingArray;
    private final int mRowDimension;
    private final int mColumnDimension;

    public Matrix(double[][] underlayingArray) {
        assert underlayingArray.length > 0;
        assert underlayingArray[0].length > 0;
        mUnderlayingArray = underlayingArray;
        mRowDimension = underlayingArray.length;
        mColumnDimension = underlayingArray[0].length;
    }

    public int getRowDimension() {
        return mRowDimension;
    }

    public int getColumnDimension() {
        return mColumnDimension;
    }

    public static Matrix createRandomMatrix(Random random, int i, int j) {
        double[][] array = allocateZeroedArray(i, j);
        for (int i1 = 0; i1 < i; i1++) {
            for (int j1 = 0; j1 < j; j1++) {
                array[i1][j1] = random.nextInt(20) - 10 + random.nextGaussian();
            }
        }
        return new Matrix(array);
    }

    public static Matrix createRandomLowerMatrix(Random random, int i) {
        double[][] array = allocateZeroedArray(i, i);
        for (int i1 = 0; i1 < i; i1++) {
            for (int j1 = 0; j1 <= i1; j1++) {
                array[i1][j1] = random.nextInt(20) - 10;
            }
        }
        return new Matrix(array);
    }

    public static Matrix createRandomUpperMatrix(Random random, int i) {
        double[][] array = allocateZeroedArray(i, i);
        for (int i1 = 0; i1 < i; i1++) {
            for (int j1 = i1; j1 < i; j1++) {
                array[i1][j1] = random.nextInt(20) - 10;
            }
        }
        return new Matrix(array);
    }

    public static Matrix load(String filePath) throws IOException {
        File file = new File(filePath);

        int rowCount = 0, colCount = 0;
        try (Scanner input = new Scanner(file)) {
            rowCount = 0;
            while (input.hasNext()) {
                if (rowCount == 0) {
                    String line = input.nextLine().replace("\t", " ").replace("  ", " ");
                    colCount = line.split(" ").length;
                } else {
                    input.nextLine();
                }
                rowCount++;
            }

        }
        if (colCount == 0) {
            throw new IOException("Invalid file content.");
        }

        try (Scanner input = new Scanner(file)) {
            double[][] rawMatrix = allocateZeroedArray(rowCount, colCount);
            int row = 0;
            while (input.hasNext()) {
                String line = input.nextLine().replace("\t", " ").replace("  ", " ");
                String[] idxs = line.split(" ");

                if (idxs.length != colCount) {
                    throw new IOException("Invalid file content.");
                }

                for (int col = 0; col < colCount; col++) {
                    rawMatrix[row][col] = Double.parseDouble(idxs[col]);
                }

                row++;
            }
            return new Matrix(rawMatrix);
        }
    }

    public static Matrix fill(int i, int j, double... vals) {
        assert vals.length == i * j;
        double[][] underlying = new double[i][j];
        int k = 0;
        for (int ii = 0; ii < i; ii++) {
            for (int jj = 0; jj < j; jj++) {
                underlying[ii][jj] = vals[k];
                k++;
            }
        }
        return new Matrix(underlying);
    }

    private static double[][] allocateZeroedArray(int i, int j) {
        return new double[i][j];
    }

    private static double[][] allocateZeroedArray(Matrix matrix) {
        return new double[matrix.mRowDimension][matrix.mColumnDimension];
    }

    private static boolean equals(double d1, double d2) {
        return Math.abs(d1 - d2) < COMPARE_THRESH;
    }

    private static void decomposeLUstep(double[][] A, int i, int n) {
        if (equals(0, A[i][i])) {
            throw new ArithmeticException("Matrix cannot be decomposed");
        }
        for (int j = i + 1; j < n; j++) {
            A[j][i] /= A[i][i];
            for (int k = i + 1; k < n; k++) {
                A[j][k] -= A[j][i] * A[i][k];
            }
        }
    }

    public static Matrix createIdentity(int n) {
        Matrix result = new Matrix(allocateZeroedArray(n, n));
        for (int i = 0; i < n; i++) {
            result.mUnderlayingArray[i][i] = 1;
        }
        return result;
    }

    public static Matrix createIdentity(Matrix m) {
        assert m.isSquareMatrix();
        return createIdentity(m.getRowDimension());
    }

    public double get(int i, int j) {
        assert i < mRowDimension;
        assert j < mColumnDimension;
        return mUnderlayingArray[i][j];
    }

    public void set(int i, int j, double value) {
        assert i < mRowDimension;
        assert j < mColumnDimension;
        mUnderlayingArray[i][j] = value;
    }

    public boolean isRowVector() {
        return mRowDimension == 1;
    }

    public boolean isColumnVector() {
        return mColumnDimension == 1;
    }

    public void save(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(filePath, "UTF-8")) {
            writer.write(toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                stringBuilder.append(get(i, j));
                stringBuilder.append(" ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    public void print() {
        System.out.println(toString());
    }

    private void assertSameSize(Matrix matrix) {
        assert mRowDimension == matrix.mRowDimension;
        assert mColumnDimension == matrix.mColumnDimension;
    }

    public Matrix times(double constant) {
        double[][] resultArray = allocateZeroedArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] * constant;
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix times(Matrix other) {
        assert mColumnDimension == other.mRowDimension;
        double[][] resultArray = allocateZeroedArray(mRowDimension, other.mColumnDimension);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < other.mColumnDimension; j++) {
                for (int k = 0; k < mColumnDimension; k++) {
                    resultArray[i][j] += mUnderlayingArray[i][k] * other.mUnderlayingArray[k][j];
                }
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix timesEquals(double constant) {
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] *= constant;
            }
        }
        return this;
    }

    public Matrix plus(double constant) {
        double[][] resultArray = allocateZeroedArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] + constant;
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix plus(Matrix other) {
        assertSameSize(other);
        double[][] resultArray = allocateZeroedArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] + other.mUnderlayingArray[i][j];
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix plusEquals(Matrix other) {
        assertSameSize(other);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] += other.mUnderlayingArray[i][j];
            }
        }
        return this;
    }

    public Matrix plusEquals(double constant) {
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] += constant;
            }
        }
        return this;
    }

    public Matrix minus(double constant) {
        double[][] resultArray = allocateZeroedArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] - constant;
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix minus(Matrix other) {
        assertSameSize(other);
        double[][] resultArray = allocateZeroedArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] - other.mUnderlayingArray[i][j];
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix minusEquals(Matrix other) {
        assertSameSize(other);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] -= other.mUnderlayingArray[i][j];
            }
        }
        return this;
    }

    public Matrix minusEquals(double constant) {
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] -= constant;
            }
        }
        return this;
    }

    public boolean isSquareMatrix() {
        return mColumnDimension == mRowDimension;
    }

    public Matrix transpose() {
        if (isSquareMatrix()) {
            double[][] resultArray = allocateZeroedArray(this);
            for (int i = 0; i < mRowDimension; i++) {
                for (int j = 0; j < mColumnDimension; j++) {
                    resultArray[j][i] = mUnderlayingArray[i][j];
                }
            }
            return new Matrix(resultArray);
        } else {
            double[][] resultArray = allocateZeroedArray(mColumnDimension, mRowDimension);
            for (int i = 0; i < mRowDimension; i++) {
                for (int j = 0; j < mColumnDimension; j++) {
                    resultArray[j][i] = mUnderlayingArray[i][j];
                }
            }
            return new Matrix(resultArray);
        }
    }

    public Matrix transposeEquals() {
        if (isSquareMatrix()) {
            double[][] resultArray = allocateZeroedArray(this);
            for (int i = 0; i < mRowDimension; i++) {
                for (int j = i + 1; j < mColumnDimension; j++) {
                    double k = mUnderlayingArray[i][j];
                    mUnderlayingArray[i][j] = mUnderlayingArray[j][i];
                    mUnderlayingArray[j][i] = k;
                }
            }
            return new Matrix(resultArray);
        } else {
            throw new UnsupportedOperationException("In place transposition can be done only on square matrices");
        }
    }

    public Matrix supstituteForward(Matrix rhsVector) {
        assert rhsVector.isColumnVector();
        assert isSquareMatrix();
        assert rhsVector.mRowDimension == mRowDimension;
        int n = mRowDimension;

        Matrix result = rhsVector.copy();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                result.mUnderlayingArray[j][0] -= mUnderlayingArray[j][i] * result.mUnderlayingArray[i][0];
            }
        }
        return result;
    }

    public Matrix supstituteBackward(Matrix rhsVector) throws ArithmeticException {
        assert rhsVector.isColumnVector();
        assert isSquareMatrix();
        assert rhsVector.mRowDimension == mRowDimension;
        int n = mRowDimension;

        Matrix result = rhsVector.copy();
        for (int i = n - 1; i >= 0; i--) {
            double pivot = mUnderlayingArray[i][i];
            if (equals(0, pivot)) {
                throw new ArithmeticException("Supstitute backward failed with zero division");
            }
            result.mUnderlayingArray[i][0] /= pivot;
            for (int j = 0; j < i; j++) {
                result.mUnderlayingArray[j][0] -= mUnderlayingArray[j][i] * result.mUnderlayingArray[i][0];
            }
        }
        return result;
    }

    public Matrix decomposeLU() throws ArithmeticException {
        assert isSquareMatrix();
        Matrix result = copy();

        for (int i = 0; i < mRowDimension - 1; i++) {
            decomposeLUstep(result.mUnderlayingArray, i, mRowDimension);
        }
        return result;
    }

    public Pair<Matrix, Matrix> decomposeLUP() throws ArithmeticException {
        assert isSquareMatrix();
        Matrix result = copy();
        double[][] A = result.mUnderlayingArray;
        int n = mRowDimension;

        Matrix permutationMatrix = Matrix.createIdentity(n);

        for (int i = 0; i < n - 1; i++) {

            int pivotIdx = i;
            double pivotAbs = Math.abs(A[i][i]);
            for (int j = i + 1; j < n; j++) {
                double curAbs = Math.abs(A[j][i]);
                if (curAbs > pivotAbs) {
                    pivotAbs = curAbs;
                    pivotIdx = j;
                }
            }
            result.swapRows(i, pivotIdx);
            permutationMatrix.swapRows(i, pivotIdx);

            decomposeLUstep(result.mUnderlayingArray, i, n);
        }
        return new Pair<>(result, permutationMatrix);
    }

    private void swapValues(int i1, int j1, int i2, int j2) {
        double k = mUnderlayingArray[i1][j1];
        mUnderlayingArray[i1][j1] = mUnderlayingArray[i2][j2];
        mUnderlayingArray[i2][j2] = k;
    }

    private void swapRows(int i1, int i2) {
        if (i1 == i2) {
            return;
        }

        for (int j = 0; j < mColumnDimension; j++) {
            swapValues(i1, j, i2, j);
        }
    }


    public Matrix extractUpper() {
        assert isSquareMatrix();
        Matrix result = copy();
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < i; j++) {
                result.mUnderlayingArray[i][j] = 0;
            }
        }
        return result;
    }

    public Matrix extractLower() {
        assert isSquareMatrix();
        Matrix result = copy();
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = i + 1; j < mColumnDimension; j++) {
                result.mUnderlayingArray[i][j] = 0;
            }
        }
        for (int i = 0; i < mRowDimension; i++) {
            result.mUnderlayingArray[i][i] = 1;
        }
        return result;
    }

    public Matrix copy() {
        double[][] copyOfArray = allocateZeroedArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            System.arraycopy(mUnderlayingArray[i], 0, copyOfArray[i], 0, mColumnDimension);
        }
        return new Matrix(copyOfArray);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        if (mRowDimension != matrix.mRowDimension) return false;
        if (mColumnDimension != matrix.mColumnDimension) return false;

        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                if (!equals(mUnderlayingArray[i][j], matrix.mUnderlayingArray[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Matrix fromVector(IVector vector) {
        int size = vector.getDimension();
        double[][] underlying = new double[size][1];
        for (int i = 0; i < size; i++) {
            underlying[i][0] = vector.get(i);
        }
        return new Matrix(underlying);
    }

    public Matrix inverse() {
        Pair<Matrix, Matrix> decomposed = decomposeLUP();
        Matrix matrix = decomposed.getKey();
        Matrix transform = decomposed.getValue();

        Matrix result = null;

        assert isSquareMatrix();

        int n = matrix.getColumnDimension();
        for (int i = 0; i < n; i++) {
            Matrix rhs = createZeroedMatrix(n, 1);
            rhs.set(i, 0, 1);
            Matrix innerResult = LinAlgUtils.solveSystem(rhs, matrix, transform);
            if (result == null) {
                result = innerResult;
            } else {
                result = result.hstack(innerResult);
            }

        }
        return result;
    }

    public Matrix hstack(Matrix matrix) {
        assert getRowDimension() == matrix.getRowDimension();
        double[][] array = Matrix.allocateZeroedArray(matrix.getRowDimension(), getColumnDimension() + matrix.getColumnDimension());
        for (int i = 0; i < getRowDimension(); i++) {
            System.arraycopy(mUnderlayingArray[i], 0, array[i], 0, getColumnDimension());
        }
        for (int i = 0; i < getRowDimension(); i++) {
            System.arraycopy(matrix.mUnderlayingArray[i], 0, array[i], getColumnDimension(), matrix.getColumnDimension());
        }
        return new Matrix(array);
    }

    public static Matrix createZeroedMatrix(int i, int j) {
        return new Matrix(Matrix.allocateZeroedArray(i, j));
    }

    public static Matrix createZeroedMatrix(Matrix asMatrix) {
        return new Matrix(Matrix.allocateZeroedArray(asMatrix));
    }


}