package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by ivan on 10/18/15.
 */
public class Prijenosna {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser(args);

        FileParser fileParser = new FileParser(argsParser.getFilePath());


        int dimension = fileParser.getSystemMatrix().getColumnDimension();
        Matrix initialSolution = Matrix.random(dimension, 1);

        IHFunction function = new TransferIHFunction(fileParser.getSystemMatrix(), fileParser.getValueVector());

        NumOptAlgorithms.runAlgorithm(initialSolution, function, argsParser.getMaxIterCount(), null, argsParser.getAlgorithm());
    }


    private static class ArgsParser {

        private final int mMaxIterCount;
        private final String mFilePath;
        private final NumOptAlgorithms.Algorithm mAlgorithm;

        public ArgsParser(String[] args) {
            if (args.length != 3) {
                System.err.printf("Parameters: algorithm max_iter_count path_to_data");
                System.exit(0);
            }

            mMaxIterCount = Integer.parseInt(args[1]);

            switch (args[0]) {
                case "newton":
                    mAlgorithm = NumOptAlgorithms.Algorithm.NEWTON_METHOD;
                    break;
                case "grad":
                default:
                    mAlgorithm = NumOptAlgorithms.Algorithm.GRADIENT_DESCENT;
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

        public NumOptAlgorithms.Algorithm getAlgorithm() {
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

            int lineCount = 0;
            try (Scanner input = new Scanner(file)) {
                while (input.hasNext()) {
                    String line = input.nextLine();
                    if (line.startsWith("#")) {
                        continue;
                    }
                    lineCount++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

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
                        sysMat = new double[lineCount][numberOfComponents];
                    }
                    if (valMat == null) {
                        valMat = new double[lineCount];
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

    private static class TransferIHFunction implements IHFunction {
        private final Matrix mSystemMatrix;
        private final Matrix mValueVector;
        private int mDimension;

        public TransferIHFunction(Matrix systemMatrix, Matrix valueVector) {
            mSystemMatrix = systemMatrix;
            mValueVector = valueVector;
            mDimension = mSystemMatrix.getColumnDimension();
        }

        @Override
        public Matrix hessianAt(Matrix point) {
            int dimension = mSystemMatrix.getColumnDimension();
            Matrix hessian = new Matrix(new double[dimension][dimension]);
            for (int i = 0; i < mSystemMatrix.getRowDimension(); i++) {
                Matrix ithHessian = new Matrix(new double[dimension][dimension]);
                Matrix xes = mSystemMatrix.getMatrix(i, i, 0, mDimension - 1).transpose();
                double c = point.get(2, 0);
                double d = point.get(3, 0);
                double e = point.get(4, 0);
                double x3 = xes.get(2, 0);
                double x4 = xes.get(3, 0);

                Matrix ithGrad = getIthGradient(point, i);
                double dc = ithGrad.get(2, 0);
                double dd = ithGrad.get(3, 0);
                double v = valueFor(point, xes) - mValueVector.get(i, 0);


                for (int j = 0; j < dimension; j++) {
                    for (int k = 0; k < dimension; k++) {
                        ithHessian.set(j, k, v == 0 ? 0 : xes.get(k, 0) / v * ithGrad.get(j, 0));
                    }
                }

                double dx3exp2 = 2 * MathExt.exp(d * x3);
                double dx3exp2sin = dx3exp2 * (-Math.sin(e * x4));
                double dx3exp2cos = dx3exp2 * (1 + Math.cos(e * x4));

                // ce
                ithHessian.set(2, 4, dx3exp2sin * x4 * (v + c * dc));
                ithHessian.set(4, 2, ithHessian.get(2, 4));
                // de
                ithHessian.set(4, 3, dx3exp2sin * x4 * c * (v * d + dd));
                ithHessian.set(3, 4, ithHessian.get(4, 3));
                // dc
                ithHessian.set(2, 3, dx3exp2cos * dd);
                ithHessian.set(3, 2, ithHessian.get(2, 3));

                hessian.plusEquals(ithHessian);
            }
            return hessian;
        }

        @Override
        public int numberOfVariables() {
            return mDimension;
        }

        @Override
        public double valueAt(Matrix point) {
            double value = 0.0;
            for (int i = 0; i < mSystemMatrix.getRowDimension(); i++) {
                Matrix xes = mSystemMatrix.getMatrix(i, i, 0, mDimension - 1).transpose();
                value += Math.pow(valueFor(point, xes) - mValueVector.get(i, 0), 2);
            }
            return value;
        }

        private double valueFor(Matrix point, Matrix xes) {
            double a = point.get(0, 0);
            double b = point.get(1, 0);
            double c = point.get(2, 0);
            double d = point.get(3, 0);
            double e = point.get(4, 0);
            double x1 = xes.get(0, 0);
            double x2 = xes.get(1, 0);
            double x3 = xes.get(2, 0);
            double x4 = xes.get(3, 0);
            double x5 = xes.get(4, 0);
            return a * x1 + b * Math.pow(x1, 3) * x2 + c * MathExt.exp(d * x3) * (1 + Math.cos(e * x4)) + e * x4 * Math.pow(x5, 2);
        }

        @Override
        public Matrix gradientAt(Matrix point) {
            Matrix gradient = new Matrix(new double[mDimension][1]);
            for (int i = 0; i < mSystemMatrix.getRowDimension(); i++) {
                Matrix ithGradient = getIthGradient(point, i);
                gradient.plusEquals(ithGradient);
            }
            return gradient;
        }

        private Matrix getIthGradient(Matrix point, int i) {
            Matrix xes = mSystemMatrix.getMatrix(i, i, 0, mDimension - 1).transpose();
            double a = point.get(0, 0);
            double b = point.get(1, 0);
            double c = point.get(2, 0);
            double d = point.get(3, 0);
            double e = point.get(4, 0);
            double x1 = xes.get(0, 0);
            double x2 = xes.get(1, 0);
            double x3 = xes.get(2, 0);
            double x4 = xes.get(3, 0);
            double x5 = xes.get(4, 0);
            double exp_d_x3 = MathExt.exp(d * x3);

            double[][] doubles = {
                    {x1},
                    {Math.pow(x1, 3) * x2},
                    {exp_d_x3 * (1 + Math.cos(e * x4))},
                    {c * (1 + Math.cos(e * x4)) * exp_d_x3 * x3},
                    {c * exp_d_x3 * (-Math.sin(e * x4)) * x4 + x4 * Math.pow(x5, 2)}
            };
            Matrix ithGradient = new Matrix(doubles);

            double v = valueFor(point, xes) - mValueVector.get(i, 0);

            ithGradient = ithGradient.times(2).times(v);
            return ithGradient;
        }
    }

    static class MathExt {
        public static double exp(double val) {
            return Math.min(Math.exp(val), Math.pow(10, 60));
        }
    }
}
