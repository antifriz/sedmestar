import hr.fer.zemris.apr.dz1.LinAlgUtils;
import hr.fer.zemris.apr.dz1.Matrix;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by ivan on 10/21/15.
 */

public class TestMatrix {
    private Random mRandom;

    @Before
    public void setUp() {
        mRandom = new Random(42);
    }

    @Test
    public void zad2() throws IOException{

        Matrix system = Matrix.load("matricazad2.txt");
        Matrix rhs=  Matrix.load("vektorzad2.txt");

        solveUsingBothMethods(system,rhs);
    }

    @Test
    public void zad3() throws IOException{

        Matrix system = Matrix.load("matricazad3.txt");
        Matrix rhs=  Matrix.load("vektorzad2.txt");

        solveUsingBothMethods(system,rhs);
    }

    @Test
    public void zad4() throws IOException{

        Matrix system = Matrix.load("matricazad4.txt");
        Matrix rhs=  Matrix.load("vektorzad4.txt");

        solveUsingBothMethods(system,rhs);
    }

    @Test
    public void zad5() throws IOException{

        Matrix system = Matrix.load("matricazad5.txt");
        Matrix rhs=  Matrix.load("vektorzad5.txt");

        solveUsingBothMethods(system,rhs);
    }

    @Test
    public void zad6() throws IOException{

        Matrix system = Matrix.load("matricazad6.txt");
        Matrix rhs=  Matrix.load("vektorzad6.txt");

        solveUsingBothMethods(system,rhs);
    }

    public void testPrecision() {
        for (int i = 0; i < 1000; i++) {
            final int m = mRandom.nextInt(10) + 1;
            final int n = mRandom.nextInt(10) + 1;

            final double d = mRandom.nextFloat();
            final double invd = 1 / d;

            Matrix firstMatrix = Matrix.createRandomMatrix(mRandom, m, n);
            Matrix firstMatrixCopy = firstMatrix.copy();

            Matrix result1 = firstMatrix.times(d).times(invd);
            assertTrue(result1.equals(firstMatrixCopy));

            Matrix result2 = firstMatrix.timesEquals(d).timesEquals(invd);
            assertTrue(result2.equals(firstMatrixCopy));
        }
    }

    public void testIO() throws IOException {
        final String pathname = "mat.mat";
        for (int i = 1; i < 50; i++) {
            for (int j = 0; j < 10; j++) {
                Matrix savedMatrix = Matrix.createRandomMatrix(mRandom, i, i);
                savedMatrix.save(pathname);
                Matrix loadedMatrix = Matrix.load(pathname);

                assertTrue(savedMatrix.equals(loadedMatrix));
            }
        }
        Files.delete(new File(pathname).toPath());
    }

    private static void solveUsingBothMethods(Matrix system, Matrix rhs){
        for(LinAlgUtils.Method method : LinAlgUtils.Method.values()){
            System.out.printf("Trying to solve with method %s\n",method.name());
            try{
                System.out.println(LinAlgUtils.solveSystem(system,rhs, method));
            }catch (ArithmeticException e){
                System.out.println(e.getMessage());
            }
        }
    }


    public void testSupstituteForward() {
        for (int i = 1; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                Matrix lowerTriangleMatrix = Matrix.createRandomLowerMatrix(mRandom, i);
                Matrix columnMatrix = Matrix.createRandomMatrix(mRandom, i, 1);
                Matrix result = lowerTriangleMatrix.supstituteForward(columnMatrix);
                assertTrue(result.isColumnVector());
            }
        }
    }

    public void testSupstituteBackward() {
        for (int i = 1; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                try {
                    Matrix upperTriangleMatrix = Matrix.createRandomUpperMatrix(mRandom, i);
                    Matrix columnMatrix = Matrix.createRandomMatrix(mRandom, i, 1);
                    Matrix result = upperTriangleMatrix.supstituteBackward(columnMatrix);
                    assertTrue(result.isColumnVector());
                }catch (ArithmeticException e){

                }
            }
        }
    }

    public void testLUDecomposition() throws IOException {
        final int n = 100;

        int counter = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n-i; j++) {
                Matrix system = Matrix.createRandomMatrix(mRandom, i, i);
                try {
                    Matrix matrix = system.decomposeLU();
                    Matrix u = matrix.extractUpper();
                    Matrix l = matrix.extractLower();

                    assertTrue(system.equals(l.times(u)));
                } catch (ArithmeticException e) {
                    counter++;
                }
            }
        }
        System.out.println("Zero divisions (%): "+counter/(double)(n*(n-1)/2));
    }

    public void testLUPDecomposition() throws IOException {
        final int n = 100;

        int counter = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n-i; j++) {
                Matrix system = Matrix.createRandomMatrix(mRandom, i, i);
                try {
                    Pair<Matrix, Matrix> pair = system.decomposeLUP();

                    Matrix matrix = pair.getKey();
                    Matrix transormation = pair.getValue();

                    Matrix u = matrix.extractUpper();
                    Matrix l = matrix.extractLower();

                    Matrix times = l.times(u);
                    Matrix times1 = transormation.times(system);
                    assertTrue(times1.equals(times));
                } catch (ArithmeticException e) {
                    counter++;
                }
            }
        }
        System.out.println("Zero divisions (%): "+counter/(double)(n*(n-1)/2));

    }

    public void testAdditionSubtraction() {
        for (int i = 0; i < 1000; i++) {
            Matrix firstMatrix = Matrix.createRandomMatrix(mRandom, 10, 10);
            Matrix firstMatrixCopy = firstMatrix.copy();
            Matrix secondMatrix = Matrix.createRandomMatrix(mRandom, 10, 10);

            Matrix result1 = firstMatrix.plus(secondMatrix).minus(secondMatrix);
            assertTrue(result1.equals(firstMatrixCopy));

            Matrix result2 = firstMatrix.plusEquals(secondMatrix).minusEquals(secondMatrix);
            assertTrue(result2.equals(firstMatrixCopy));
        }
    }

    public void testtimes() {
        for (int i = 0; i < 1000; i++) {
            final int m = mRandom.nextInt(10) + 1;
            final int n = mRandom.nextInt(10) + 1;
            final int o = mRandom.nextInt(10) + 1;

            final double d = mRandom.nextFloat();
            final double invd = 1 / d;

            Matrix firstMatrix = Matrix.createRandomMatrix(mRandom, m, n);
            Matrix firstMatrixCopy = firstMatrix.copy();
            Matrix secondMatrix = Matrix.createRandomMatrix(mRandom, n, o);

            Matrix result1 = firstMatrix.times(d).times(invd);
            assertTrue(result1.equals(firstMatrixCopy));

            Matrix result2 = firstMatrix.timesEquals(d).timesEquals(invd);
            assertTrue(result2.equals(firstMatrixCopy));

            Matrix result3 = firstMatrix.times(secondMatrix);
        }
    }

}
