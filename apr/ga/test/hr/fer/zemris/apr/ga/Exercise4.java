package hr.fer.zemris.apr.ga;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivan on 1/3/16.
 */
public class Exercise4 {
    public static final double DESIRED_ERROR = Math.pow(10, -6);
    public static final int TEST_CNT = 100;

    @FunctionalInterface
    interface Tweaker<T> {
        void tweak(ArgsHolder holder, T param);
    }

    @Test
    public void testPopCnt() throws Exception {
        Integer[] params = new Integer[]{15,20,30,50,100,200};
        Tweaker<Integer> tweaker = (holder, param) -> holder.popCnt = param;
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = 6;
        argsHolder.desiredError = -1;
        argsHolder.verbose = false;
        argsHolder.isRoulette = false;
        argsHolder.tournamentCnt=3;
        argsHolder.maxIter = 1000;
        tweakParam(argsHolder, params, tweaker);
    }

    private <T> void tweakParam(ArgsHolder argsHolder ,T[] params, Tweaker<T> tweaker) {
        List<double[]> resultss = new ArrayList<>();
        for (T param : params) {

            tweaker.tweak(argsHolder,param);
            double[] results = new double[TEST_CNT];
            int goodOnes = 0;
            for (int j = 0; j < results.length; j++) {
                results[j] = GeneticAlgorithm.run(argsHolder);
                if (results[j] < DESIRED_ERROR) {
                    goodOnes++;
                }
            }
            resultss.add(results);
            System.out.println("PARAM VAL: "+param);
            System.out.println("MEDIAN: " + results[TEST_CNT / 2]);
            System.out.println("HIT RATE: " + goodOnes / (float) TEST_CNT);
        }
        NumberFormat format = new DecimalFormat("#0.000000000000000000");
        String s = Arrays.toString(params);
        System.out.println(s.substring(1,s.length()-1));
        for (int i = 0; i < TEST_CNT; i++) {
            for (int j = 0; j < params.length; j++) {
                System.out.print(format.format(resultss.get(j)[i]));
                if(j<params.length-1){
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testMutation() throws Exception {
        Double[] params = new Double[]{.2,.3,.6,.9};
        Tweaker<Double> tweaker = (holder, param) -> holder.mutationProba = param;
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = 6;
        argsHolder.desiredError = -1;
        argsHolder.verbose = false;
        argsHolder.isRoulette = false;
        argsHolder.tournamentCnt=3;
        argsHolder.maxIter = 500;
        argsHolder.popCnt=50;
        tweakParam(argsHolder,params, tweaker);
    }
}
