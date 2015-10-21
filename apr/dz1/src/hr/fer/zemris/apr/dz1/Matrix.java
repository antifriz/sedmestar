package hr.fer.zemris.apr.dz1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by ivan on 10/19/15.
 */
public final class Matrix {
    private static final double COMPARE_THRESH = Math.pow(10, -6);
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


    public int getRowDimension() {
        return mRowDimension;
    }

    public int getColumnDimension() {
        return mColumnDimension;
    }

    public Matrix getRow(int i) {
        return null;
    }

    public Matrix getColumn(int j) {
        return null;
    }

    public void setRow(int i, Matrix matrix) {

    }

    public void setColumn(int j, Matrix matrix) {

    }

    public void replaceColumns(int j1, int j2) {

    }

    public void replaceRows(int i1, int i2) {

    }

    public boolean isRowVector() {
        return mRowDimension == 1;
    }

    public boolean isColumnVector() {
        return mColumnDimension == 1;
    }

    public static Matrix createRandomMatrix(Random random, int i, int j) {
        double[][] array = allocateArray(i, j);
        for (int i1 = 0; i1 < i; i1++) {
            for (int j1 = 0; j1 < j; j1++) {
                array[i1][j1] = random.nextInt(20) - 10;
            }
        }
        return new Matrix(array);
    }

    public static Matrix createRandomLowerMatrix(Random random, int i) {
        double[][] array = allocateArray(i, i);
        for (int i1 = 0; i1 < i; i1++) {
            for (int j1 = 0; j1 <= i1; j1++) {
                array[i1][j1] = random.nextInt(20) - 10;
            }
        }
        return new Matrix(array);
    }

    public static Matrix createRandomUpperMatrix(Random random, int i) {
        double[][] array = allocateArray(i, i);
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
            double[][] rawMatrix = allocateArray(rowCount, colCount);
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

    private static double[][] allocateArray(int i, int j) {
        return new double[i][j];
    }

    private static double[][] allocateArray(Matrix matrix) {
        return new double[matrix.mRowDimension][matrix.mColumnDimension];
    }

    private void assertSameSize(Matrix matrix) {
        assert mRowDimension == matrix.mRowDimension;
        assert mColumnDimension == matrix.mColumnDimension;
    }

    public Matrix multiply(double constant) {
        double[][] resultArray = allocateArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] * constant;
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix multiply(Matrix other) {
        assert mColumnDimension == other.mRowDimension;
        double[][] resultArray = allocateArray(mRowDimension, other.mColumnDimension);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < other.mColumnDimension; j++) {
                for (int k = 0; k < mColumnDimension; k++) {
                    resultArray[i][j] = mUnderlayingArray[i][k] * other.mUnderlayingArray[k][j];
                }
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix multiplyEquals(double constant) {
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] *= constant;
            }
        }
        return this;
    }

    public Matrix plus(double constant) {
        double[][] resultArray = allocateArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] + constant;
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix plus(Matrix other) {
        assertSameSize(other);
        double[][] resultArray = allocateArray(this);
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
        double[][] resultArray = allocateArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                resultArray[i][j] = mUnderlayingArray[i][j] - constant;
            }
        }
        return new Matrix(resultArray);
    }

    public Matrix minus(Matrix other) {
        assertSameSize(other);
        double[][] resultArray = allocateArray(this);
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
            double[][] resultArray = allocateArray(this);
            for (int i = 0; i < mRowDimension; i++) {
                for (int j = 0; j < mColumnDimension; j++) {
                    resultArray[j][i] = mUnderlayingArray[i][j];
                }
            }
            return new Matrix(resultArray);
        } else {
            double[][] resultArray = allocateArray(mColumnDimension, mRowDimension);
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
            double[][] resultArray = allocateArray(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        if (mColumnDimension != matrix.mColumnDimension || mRowDimension != matrix.mRowDimension) {
            return false;
        }
        for (int i = 0; i < mRowDimension; i++) {
            for (int j = 0; j < mColumnDimension; j++) {
                if (!equals(mUnderlayingArray[i][j], matrix.mUnderlayingArray[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean equals(double d1, double d2) {
        return Math.abs(d1 - d2) < COMPARE_THRESH;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(mUnderlayingArray);
    }

    public Matrix supstituteForward(Matrix rhsVector) {
        assert rhsVector.isColumnVector();
        assert isSquareMatrix();
        assert rhsVector.mRowDimension == mRowDimension;

        double outputVector[][] = Matrix.allocateArray(mRowDimension, 1);
        for (int i = 0; i < mRowDimension; i++) {
            outputVector[i][0] = rhsVector.get(i, 0);

            for (int j = 0; j < i; j++) {
                outputVector[i][0] -= get(i, j) * outputVector[j][0];
            }
            outputVector[i][0] /= get(i, i);
        }
        return new Matrix(outputVector);
    }

    public Matrix supstituteBackward(Matrix rhsVector) {
        assert rhsVector.isColumnVector();
        assert isSquareMatrix();
        assert rhsVector.mRowDimension == mRowDimension;

        double outputVector[][] = Matrix.allocateArray(mRowDimension, 1);
        for (int i = mRowDimension - 1; i >= 0; i--) {
            outputVector[i][0] = rhsVector.get(i, 0);

            for (int j = mRowDimension - 1; j > i; j--) {
                outputVector[i][0] -= get(i, j) * outputVector[j][0];
            }
            outputVector[i][0] /= get(i, i);
        }
        return new Matrix(outputVector);
    }

    public Matrix decomposeLU() {
        assert isSquareMatrix();
        Matrix result = copy();
        for (int i = 0; i < mRowDimension; i++) {
            result.decomposeLU(i);
        }
        return result;
    }

    private void decomposeLU(int step) {
        double pivot = mUnderlayingArray[step][step];
        for (int i = step+1; i < mRowDimension; i++) {
            mUnderlayingArray[step][i] /= pivot;
        }
        for (int i = step+1; i < mRowDimension; i++) {
            for (int j = step+1; j < mColumnDimension; j++) {
                mUnderlayingArray[i][j] += mUnderlayingArray[i][step] + mUnderlayingArray[step][j];
            }
        }
    }

    public Matrix extractUpper(){
        return null;
    }

    public Matrix extractLower(){
        return null;
    }

    public Matrix decomposeLUP() {
        assert isSquareMatrix();
        Matrix result = copy();
        double[][] permutation = allocateArray(this);
        for (int i = 0; i < mRowDimension; i++) {
            double curMaxVal = mUnderlayingArray[i][i];
            int curMaxIdx = i;
            for (int j = i+1; j < mRowDimension; j++) {
                if(curMaxVal<mUnderlayingArray[j][i]){
                    curMaxVal = mUnderlayingArray[j][i];
                    curMaxIdx = j;
                }
            }
            permutation[i][curMaxIdx] =1;
            result.decomposeLU(i);
        }
        return result.multiply(new Matrix(permutation));
    }

    public Matrix copy() {
        return new Matrix(Arrays.copyOf(mUnderlayingArray, mUnderlayingArray.length));
    }
}