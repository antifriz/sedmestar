package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ivan on 10/10/15.
 */
public class Algorithm2 implements Algorithm {

    private static final int MAX_ITER_COUNT = 100000;
    private final Random mRandom;
    private SATFormulaStats mStats;

    public Algorithm2() {
        mRandom = new Random();
    }

    @Override
    public void solveSATProblem(SATFormula formula) {

        mStats = new SATFormulaStats(formula);

        BitVector candidateBitVector = new BitVector(mRandom, formula.getNumberOfVariables());

        int iterCount = 0;
        do {
            BitVector newCandidate = pickNewCandidate(candidateBitVector);
            if (newCandidate == candidateBitVector) {
                System.out.println("Dokazivanje nije uspjelo (zaglavljen u lokalnom minimumu)");
                break;
            }
            candidateBitVector = newCandidate;
            iterCount++;
        } while (isStopConditionSatisfied(iterCount, candidateBitVector));
    }

    private boolean isStopConditionSatisfied(int iterCount, BitVector candidateBitVector) {
        mStats.setAssignment(candidateBitVector, false);
        System.out.printf("[Iter %6d] -> Satisfied: %4d/%4d Candidate: %s\n", iterCount, mStats.getNumberOfSatisfied(), mStats.getNumberOfClauses(), candidateBitVector);
        if (iterCount >= MAX_ITER_COUNT) {
            System.out.println("Dokazivanje nije uspjelo (premasen dozvoljeni broj iteracija)");
            return false;
        }
        if (mStats.isSatisfied()) {
            System.out.printf("Zadovoljivo: %s\n", candidateBitVector);
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

        for (BitVector neighbour : generator) {
            int neighbourFitness = calculateFitness(neighbour);
            if (neighbourFitness == bestFitnessSoFar) {
                bestCandidates.add(neighbour);
            } else if (neighbourFitness > bestFitnessSoFar) {
                bestCandidates.clear();
                bestCandidates.add(neighbour);
                bestFitnessSoFar = neighbourFitness;
            }
        }

        if (bestFitnessSoFar < parentFitness) {
            return null;
        }
        return bestCandidates.get(mRandom.nextInt(bestCandidates.size()));
    }

    private int calculateFitness(BitVector candidate) {
        mStats.setAssignment(candidate, false);
        return mStats.getNumberOfSatisfied();
    }


}
