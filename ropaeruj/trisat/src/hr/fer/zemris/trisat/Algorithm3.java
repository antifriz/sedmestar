package hr.fer.zemris.trisat;

import java.util.*;

/**
 * Created by ivan on 10/10/15.
 */
public class Algorithm3 implements Algorithm {
    private static final int NUMBER_OF_BEST = 3;
    public static final int MAX_ITER_COUNT = 10000;
    private SATFormulaStats mStats;
    private Random mRandom;

    public Algorithm3() {
        mRandom = new Random();
    }

    @Override
    public void solveSATProblem(SATFormula formula) {

        mStats = new SATFormulaStats(formula);

        BitVector candidateBitVector = new BitVector(mRandom,formula.getNumberOfVariables());

        int iterCount = 0;
        do {
            mStats.setAssignment(candidateBitVector,true);
            candidateBitVector= pickNewCandidate(candidateBitVector);
            iterCount++;
        } while (isStopConditionSatisfied(iterCount, candidateBitVector));
    }

    private boolean isStopConditionSatisfied(int iterCount, BitVector candidateBitVector) {
        mStats.setAssignment(candidateBitVector,false);
        System.out.printf("[Iter %6d] -> Satisfied: %4d/%4d Candidate: %s\n",iterCount,mStats.getNumberOfSatisfied(),mStats.getNumberOfClauses(),candidateBitVector);

        if (iterCount >= MAX_ITER_COUNT) {
            System.out.println("Dokazivanje nije uspjelo (premasen dozvoljeni broj iteracija)");
            return false;
        }
        if (mStats.isSatisfied()) {
            System.out.printf("Zadovoljivo: %s\n",candidateBitVector);
            return false;
        }
        return true;
    }
    
    private class BitVectorCorrectionBundle { 
	  private final BitVector bitVector;
	  private final double correction;
	 
	  public BitVectorCorrectionBundle(BitVector bitVector, double correction) {
	    this.bitVector = bitVector;
	    this.correction = correction;
	  } 
	} 

    private BitVector pickNewCandidate(BitVector parent) {
        BitVectorNGenerator generator = new BitVectorNGenerator(parent);
        List<BitVectorCorrectionBundle> candidates = new ArrayList<>();

        
        for(BitVector candidate: generator){
            mStats.setAssignment(candidate,false);
            int numberOfSatisfied =  mStats.getNumberOfSatisfied();
            double percentageBonus = mStats.getPercentageBonus();
            double correction = numberOfSatisfied + percentageBonus;
            candidates.add(new BitVectorCorrectionBundle(candidate, correction));
        }

        candidates.sort((x1,x2)->Double.compare(x2.correction,x1.correction));

        candidates = candidates.subList(0,NUMBER_OF_BEST);

        return candidates.subList(0,NUMBER_OF_BEST).get(mRandom.nextInt(candidates.size())).bitVector;
    }
}
