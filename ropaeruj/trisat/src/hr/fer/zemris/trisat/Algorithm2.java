package hr.fer.zemris.trisat;

import java.util.*;

/**
 * Created by ivan on 10/10/15.
 */
public class Algorithm2 implements Algorithm {

    private final Random mRandom;
    private SATFormula mFormula;

    public Algorithm2() {
        mRandom = new Random();
    }

    @Override
    public SATProblemResults solveSATProblem(SATFormula formula) {

        mFormula = formula;

        BitVector candidateBitVector = new BitVector(mRandom,mFormula.getNumberOfVariables());

        int iterCount = 0;
        do {
            BitVector newCandidate= pickNewCandidate(candidateBitVector);
            if(newCandidate == candidateBitVector){
                break;
            }
            candidateBitVector = newCandidate;
            iterCount++;
        } while (isStopConditionSatisfied(iterCount, candidateBitVector));


        SATProblemResults results = new SATProblemResults();
        results.addResult(candidateBitVector);
        return results;
    }

    private boolean isStopConditionSatisfied(int iterCount, BitVector candidateBitVector) {
        if (candidateBitVector == null) {
            System.out.println("Stuck in local minima");
            return false;
        }
        System.out.printf("[Iter %06d] -> Score: %2d Candidate: %s\n",iterCount,mFormula.numberOfSatisfiedClauses(candidateBitVector),candidateBitVector);
        if (iterCount >= 100000) {
            System.out.println("Iter count overflowed");
            return false;
        }
        if (mFormula.isSatisfied(candidateBitVector)) {
            System.out.println("Formula is satisfied");
            return false;
        }
        return true;
    }

    private BitVector pickNewCandidate(BitVector parent) {
        BitVectorNGenerator generator = new BitVectorNGenerator(parent);
        List<BitVector> bestCandidates = new ArrayList<>();
        bestCandidates.add(parent);
        int parentFitness = calculateFitness(parent);
        int bestFitnessSoFar = parentFitness;

        for(BitVector neighbour: generator){
            int neighbourFitness = calculateFitness(neighbour);
            if (neighbourFitness == bestFitnessSoFar) {
                bestCandidates.add(neighbour);
            } else if (neighbourFitness > bestFitnessSoFar) {
                bestCandidates.clear();
                bestCandidates.add(neighbour);
                bestFitnessSoFar = neighbourFitness;
            }
//            System.out.printf("Score: %3s Best so far: %3s Candidate: %s\n",neighbourFitness,bestFitnessSoFar,candidate);
        }
//        System.out.println("Chosen");
//        for (BitVector candidate:
//        bestCandidates) {
//            System.out.printf("Score: %3s Candidate: %s\n",mFormula.numberOfSatisfiedClauses(candidate),candidate);
//        }
        if (bestFitnessSoFar < parentFitness) {
            return null;
        }
        return bestCandidates.get(mRandom.nextInt(bestCandidates.size()));
    }

    private int calculateFitness(BitVector candidate) {
        return mFormula.numberOfSatisfiedClauses(candidate);
    }


}
