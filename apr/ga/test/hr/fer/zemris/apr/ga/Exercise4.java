package hr.fer.zemris.apr.ga;

import org.junit.Test;

/**
 * Created by ivan on 1/3/16.
 */
public class Exercise4 extends TweakableExercise {

    @Test
    public void testPopCnt() throws Exception {
        Integer[] params = new Integer[]{3, 5, 10, 15, 20, 30, 50, 100, 200};
        Tweaker<Integer> tweaker = (holder, param) -> {
            holder.popCnt = param;
            holder.maxIter = 7000 / param;
        };
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = 6;
        argsHolder.desiredError = -1;
        argsHolder.verbose = false;
        argsHolder.isRoulette = false;
        argsHolder.tournamentCnt = 3;
        tweakParam(argsHolder, params, tweaker);
    }


    @Test
    public void testMutation() throws Exception {
        Double[] params = new Double[]{.05,.1,.2, .3, .6, .9};
        Tweaker<Double> tweaker = (holder, param) -> holder.mutationProba = param;
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = 6;
        argsHolder.desiredError = -1;
        argsHolder.verbose = false;
        argsHolder.isRoulette = false;
        argsHolder.tournamentCnt = 3;
        argsHolder.maxIter = 250;
        argsHolder.popCnt = 10;
        tweakParam(argsHolder, params, tweaker);
    }

    // idealni VEL_POP = 50, mutation_proba = 0.4
}
