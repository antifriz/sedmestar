package hr.fer.zemris.apr.ga;


/**
 * Created by ivan on 10/23/15.
 */
final class ArgsHolder {
    public int functionIdx = 0;
    public int desiredDim = 2;
    public Integer popCnt = 15;
    public boolean isBinary=false;
    public int bitCount = 10;
    public double mutationProba = 0.1;
    public int maxIter = 100000;
    public boolean isRoulette = false;
    public int tournamentCnt = 3;
    public double desiredError = Math.pow(10,-6);
    public boolean verbose = true;
}
