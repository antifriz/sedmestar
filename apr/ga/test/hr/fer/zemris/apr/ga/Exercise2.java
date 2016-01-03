package hr.fer.zemris.apr.ga;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by ivan on 1/3/16.
 */
@RunWith(Parameterized.class)
public class Exercise2 {
    private Integer mFunctionIdx;
    private Integer mDesiredDim;
    private Boolean mIsBinary;

    @Test
    public void test() throws Exception {
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = mFunctionIdx;
        argsHolder.desiredDim = mDesiredDim;
        argsHolder.isBinary = mIsBinary;
        for (int i = 0; i < 5; i++) {
            if (GeneticAlgorithm.run(argsHolder) < Math.pow(10, -6)) {
                return;
            }
        }
        Assert.assertTrue(false);
    }

    public Exercise2(Integer functionIdx, Integer desiredDim, Boolean isBinary) {
        mFunctionIdx = functionIdx;

        mDesiredDim = desiredDim;
        mIsBinary = isBinary;
    }

    @Parameterized.Parameters(name = "f{0}, dim = {1}")
    public static Collection params() {

        boolean isBinary = false;
        return Arrays.asList(new Object[][]{
                {6, 1, isBinary},
                {6, 3, isBinary},
                {6, 6, isBinary},
                {6, 10, isBinary},
                {7, 1, isBinary},
                {7, 3, isBinary},
                {7, 6, isBinary},
                {7, 10, isBinary}
        });
    }
}
