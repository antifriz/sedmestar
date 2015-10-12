package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ivan on 10/10/15.
 *
 * Implementation with iterative algorithm that uses simple fitness function
 *
 * It can easily stuck in local optima.
 */
public class Algorithm2 implements Algorithm {

    private static final int MAX_ITER_COUNT = 100000;
    private final Random mRandom;
    private SATFormulaStats mStats;

    public Algorithm2() {
        mRandom = new Random();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Method that checks satisfaction of stop condition in main loop.
     * Returns false if <code>MAX_ITER_COUNT</code> is exceeded or formula is satisfied
     *
     * @param iterCount          current iteration count
     * @param candidateBitVector current vector
     * @return whether stop condition is satisfied
     */
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

    /**
     * Method for picking new candidate from neighbourhood for given parent vector based on fitness calculation.
     * From neighbours with the same fitness, a random one is chosen.
     *
     * @param parent last candidate vector
     * @return new candidate vector
     */
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

    /**
     * Used to calculate fitness for given candidate.
     * Fitness is calculated as number of clauses that are satisfied by candidate.
     *
     * @param candidate candidate to calculate fitness for
     * @return fitness
     */
    private int calculateFitness(BitVector candidate) {
        mStats.setAssignment(candidate, false);
        return mStats.getNumberOfSatisfied();
    }


}
