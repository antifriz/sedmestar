package hr.fer.zemris.apr.ga;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivan on 1/3/16.
 */
public abstract class TweakableExercise {

    @FunctionalInterface
    protected  interface Tweaker<T> {
        void tweak(ArgsHolder holder, T param);
    }

    protected  <T> void tweakParam(ArgsHolder argsHolder ,T[] params, Tweaker<T> tweaker) {
        List<double[]> resultss = new ArrayList<>();
        for (T param : params) {

            tweaker.tweak(argsHolder,param);
            double[] results = new double[getTestCnt()];
            int goodOnes = 0;
            for (int j = 0; j < results.length; j++) {
                results[j] = GeneticAlgorithm.run(argsHolder);
                if (results[j] < getDesiredError()) {
                    goodOnes++;
                }
            }
            resultss.add(results);
            System.out.println("PARAM VAL: "+param);
            System.out.println("MEDIAN: " + results[getTestCnt() / 2]);
            System.out.println("HIT RATE: " + goodOnes / (float) getTestCnt());
        }
        NumberFormat format = new DecimalFormat("#0.000000000000000000");
        String s = Arrays.toString(params);
        System.out.println(s.substring(1,s.length()-1));
        for (int i = 0; i < getTestCnt(); i++) {
            for (int j = 0; j < params.length; j++) {
                System.out.print(format.format(resultss.get(j)[i]));
                if(j<params.length-1){
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    protected double getDesiredError(){
        return Math.pow(10,-6);
    }

    protected int getTestCnt(){
        return 100;
    }
}
