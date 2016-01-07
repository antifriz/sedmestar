package hr.fer.zemris.apr.ga;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ivan on 1/3/16.
 */
public class CustomExercise {
    @Test
    public void test() throws Exception {
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = 7;
        argsHolder.desiredDim = 2;
        argsHolder.isBinary = true;
        argsHolder.bitCount=21;
        argsHolder.mutationProba = 0.1;


        for (int i = 0; i < 10; i++) {
            if(GeneticAlgorithm.run(argsHolder)<=Math.pow(10,-6)){
                System.out.println("Number of attempts: "+(i+1));
                return;
            }
        }
        Assert.assertTrue(false);
    }


}
