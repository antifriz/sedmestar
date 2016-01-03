package hr.fer.zemris.apr.ga;

import org.junit.Test;

/**
 * Created by ivan on 1/3/16.
 */
public class Exercise5 extends TweakableExercise {

    @Test
    public void testTournamentK() throws Exception {
        Integer[] params = new Integer[]{3, 4, 5, 10, 15, 20, 30, 50};
        Tweaker<Integer> tweaker = (holder, param) -> {
            holder.tournamentCnt = param;
            holder.maxIter = 1000 / param;
        };
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.popCnt = 10;
        argsHolder.mutationProba = 0.9;
        argsHolder.functionIdx = 6;
        argsHolder.desiredError = -1;
        argsHolder.verbose = false;
        argsHolder.isRoulette = false;
        argsHolder.maxIter = 100;
        tweakParam(argsHolder, params, tweaker);
    }

}
