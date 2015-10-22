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

    @Test
    public void testIO() throws IOException {
        final String pathname = "mat.mat";
        for (int i = 1; i < 50; i++) {
            for (int j = 0; j < 10; j++) {
                Matrix savedMatrix = Matrix.createRandomMatrix(mRandom,i,i);
                savedMatrix.save(pathname);
                Matrix loadedMatrix = Matrix.load(pathname);

                assertTrue(savedMatrix.equals(loadedMatrix));
            }
        }
        Files.delete(new File(pathname).toPath());
    }

    @Test
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

    @Test
    public void testSupstituteBackward() {
        for (int i = 1; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                Matrix upperTriangleMatrix = Matrix.createRandomUpperMatrix(mRandom, i);
                Matrix columnMatrix = Matrix.createRandomMatrix(mRandom, i, 1);
                Matrix result = upperTriangleMatrix.supstituteBackward(columnMatrix);
                assertTrue(result.isColumnVector());
            }
        }
    }

    @Test
    public void testLUdecomposition() throws IOException{
        Matrix system = Matrix.load("matricazad2.txt");

        Matrix matrix = system.decomposeLU();
        Matrix u = matrix.extractUpper();
        Matrix l = matrix.extractLower();

        assertTrue(system.equals(l.times(u)));
    }
    @Test
    public void testLUPdecomposition() throws IOException{
        Matrix system = Matrix.load("matricazad2.txt");

        Pair<Matrix,Matrix> pair = system.decomposeLUP();

        Matrix matrix = pair.getKey();
        Matrix transormation = pair.getValue();

        Matrix u = matrix.extractUpper();
        Matrix l = matrix.extractLower();

        Matrix times = transormation.times(l.times(u));
        assertTrue(system.equals(times));
    }

    @Test
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

    @Test
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
