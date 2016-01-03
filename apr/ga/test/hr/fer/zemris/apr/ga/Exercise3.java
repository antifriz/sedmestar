package hr.fer.zemris.apr.ga;

import org.junit.Test;

/**
 * Created by ivan on 1/3/16.
 */
public class Exercise3 extends TweakableExercise {
    public void test(int idx) throws Exception {
        Boolean[] params = new Boolean[]{false, true};
        Tweaker<Boolean> tweaker = (holder, param) -> holder.isBinary = param;
        ArgsHolder argsHolder = new ArgsHolder();
        argsHolder.functionIdx = idx;
        argsHolder.desiredError = -1;
        argsHolder.verbose = false;
        argsHolder.isRoulette = false;
        argsHolder.tournamentCnt = 3;
        argsHolder.bitCount = 4;
        argsHolder.maxIter = 10000;
        argsHolder.popCnt = 10;
        tweakParam(argsHolder, params, tweaker);
    }

    @Test
    public void test6() throws Exception {
        test(6);

    }

    @Test
    public void test7() throws Exception {
        test(7);
    }

    @Override
    protected int getTestCnt() {
        return 5;
    }
}
