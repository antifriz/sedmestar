package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.NumOptAlgorithms.Algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by ivan on 10/18/15.
 */
public class Sustav {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser(args);

        FileParser fileParser = new FileParser(argsParser.getFilePath());


        int dimension = fileParser.getValueVector().getRowDimension();
//        Matrix initialSolution = Matrix.random(dimension,1);
        double [] values = new double[dimension];
        Matrix initialSolution = new Matrix(values,1).transpose();

        IHFunction function = new SystemIHFunction( fileParser.getSystemMatrix(),fileParser.getValueVector());

        NumOptAlgorithms.runAlgorithm(initialSolution, function, argsParser.getMaxIterCount(), null, argsParser.getAlgorithm());
    }


    private static class ArgsParser {

        private final int mMaxIterCount;
        private final String mFilePath;
        private final Algorithm mAlgorithm;

        public ArgsParser(String[] args) {
            if (args.length != 3) {
                System.err.printf("Parameters: algorithm max_iter_count path_to_data");
                System.exit(0);
            }

            mMaxIterCount = Integer.parseInt(args[1]);

            switch (args[0]) {
                case "newton":
                    mAlgorithm = Algorithm.NEWTON_METHOD;
                    break;
                case "grad":
                default:
                    mAlgorithm = Algorithm.GRADIENT_DESCENT;
                    break;
            }

            mFilePath = args[2];
        }

        public int getMaxIterCount() {
            return mMaxIterCount;
        }

        public String getFilePath() {
            return mFilePath;
        }

        public Algorithm getAlgorithm() {
            return mAlgorithm;
        }
    }

    private static class FileParser {

        private Matrix mSystemMatrix;
        private Matrix mValueVector;

        public FileParser(String filePath) {
            File file = new File(filePath);

            mSystemMatrix = null;
            mValueVector = null;

            try (Scanner input = new Scanner(file)) {
                double[][] sysMat = null;
                double[] valMat = null;
                int row = 0;
                while (input.hasNext()) {
                    String line = input.nextLine().replace(" ", "");
                    if (line.startsWith("#")) {
                        continue;
                    }
                    line = line.substring(1, line.length() - 1);
                    String[] idxs = line.split(",");

                    int numberOfComponents = idxs.length - 1;
                    if (sysMat == null) {
                        sysMat = new double[numberOfComponents][numberOfComponents];
                    }
                    if (valMat == null) {
                        valMat = new double[numberOfComponents];
                    }

                    for (int col = 0; col < numberOfComponents; col++) {
                        sysMat[row][col] = Double.parseDouble(idxs[col]);
                    }

                    valMat[row] = Double.parseDouble(idxs[numberOfComponents]);

                    row++;
                }
                mSystemMatrix = new Matrix(sysMat);
                mValueVector = new Matrix(valMat, 1).transpose();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public Matrix getSystemMatrix() {
            return mSystemMatrix;
        }

        public Matrix getValueVector() {
            return mValueVector;
        }
    }

    private static class SystemIHFunction implements IHFunction {
        private final Matrix mIdentityMatrix;
        private final Matrix mSumOfComponentsMatrix;
        private Matrix mOnesRowVector;

        private final int mDimension;
        private final Matrix mSystemMatrix;
        private Matrix mValueVector;

        public SystemIHFunction(Matrix systemMatrix, Matrix valueVector) {
            mSystemMatrix = systemMatrix;
            mValueVector = valueVector;
            mDimension = mValueVector.getRowDimension();

            double[] ones = new double[mDimension];
            Arrays.fill(ones,1);
            mOnesRowVector = new Matrix(ones,1);
            mIdentityMatrix = Matrix.identity(mDimension,mDimension);
            mSumOfComponentsMatrix = mIdentityMatrix.copy();
            Matrix vectorSummer = mOnesRowVector.times(mSystemMatrix);
            for (int i = 0; i < mDimension; i++) {
                mSumOfComponentsMatrix.set(i,i,vectorSummer.get(0,i));
            }
        }

        @Override
        public Matrix hessianAt(Matrix point) {
            return null;
        }

        @Override
        public int numberOfVariables() {
            return mDimension;
        }

        @Override
        public double valueAt(Matrix point) {
            Matrix values = mSystemMatrix.times(point);
            Matrix squaredValues = values.times(values);
            return MatrixUtils.asScalar(squaredValues);
        }

        @Override
        public Matrix gradientAt(Matrix point) {
            Matrix calcValueVector = mSystemMatrix.times(point);
//            System.out.println("calcvaluevector" + MatrixUtils.prettyPrintVector(calcValueVector));
            Matrix offsetVector = calcValueVector.minus(mValueVector);
//            System.out.println("offsetvector" + MatrixUtils.prettyPrintVector(offsetVector));
            Matrix gradient = mSystemMatrix.transpose().times(offsetVector).times(2);
//            System.out.println("gradient" + MatrixUtils.prettyPrintVector(gradient));
            MatrixUtils.assertIsColumnVector(gradient);
            return gradient;
        }
    }
}
