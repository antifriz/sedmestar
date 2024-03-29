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
public class Exercise1 {
    private Integer mFunctionIdx;
    private Integer mDesiredDim;
    private Boolean mIsBinary;

    @Test
    public void test() throws Exception {
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = mFunctionIdx;
        argsHolder.desiredDim = mDesiredDim;
        argsHolder.isBinary = mIsBinary;
        argsHolder.mutationProba = 0.05;
        argsHolder.bitCount =28;

        for (int i = 0; i < 10; i++) {
            if(GeneticAlgorithm.run(argsHolder)<=Math.pow(10,-6)){
                System.out.println("Number of attempts: "+(i+1));
                return;
            }
        }
        Assert.assertTrue(false);
    }


    public Exercise1(Integer functionIdx, Integer desiredDim,Boolean isBinary) {
        mFunctionIdx = functionIdx;

        mDesiredDim = desiredDim;
        mIsBinary = isBinary;
    }

    @Parameterized.Parameters(name="f{0}, dim = {1}")
    public static Collection params(){

        boolean isBinary = true;
        return Arrays.asList(new Object[][] {
                { 1,2, isBinary},
                { 3,5, isBinary},
                { 6,2, isBinary},
                { 7,2, isBinary}
        });
    }
}
