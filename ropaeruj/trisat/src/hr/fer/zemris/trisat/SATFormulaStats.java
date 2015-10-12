package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 */
public class SATFormulaStats {
    private static final double PERCENTAGE_CONSTANT_UP = 0.01;
    private static final double PERCENTAGE_CONSTANT_DOWN = 0.1;
    private static final double PERCENTAGE_UNIT_AMOUNT = 50;
    private final SATFormula mFormula;
    private final double[] mPost;
    private int mNumberOfSatisfied;
    private boolean mIsSatisfied;
    private double mPercentageBonus;


    /**
     * @param formula formula to calculate stats for
     */
    SATFormulaStats(SATFormula formula) {
        mFormula = formula;
        mPost = new double[formula.getNumberOfClauses()];
    }

    /**
     * Stats calculation for given vector based on formula given in lab assignment
     *
     * @param assignment        vector to calculate stats for
     * @param updatePercentages whether percentage data will be calculated
     */
    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        int satisfiedCount = 0;
        int numberOfClauses = mFormula.getNumberOfClauses();
        for (int i = 0; i < numberOfClauses; i++) {
            Clause clause = mFormula.getClause(i);
            if (clause.isSatisified(assignment)) {
                satisfiedCount++;
                if (updatePercentages) {
                    mPost[i] += (1.0 - mPost[i]) * PERCENTAGE_CONSTANT_UP;
                }
            } else if (updatePercentages) {
                mPost[i] += (0.0 - mPost[i]) * PERCENTAGE_CONSTANT_DOWN;
            }
        }
        mNumberOfSatisfied = satisfiedCount;
        mIsSatisfied = satisfiedCount == numberOfClauses;

        double correction = 0.0;
        for (int i = 0; i < numberOfClauses; i++) {
            Clause clause = mFormula.getClause(i);
            double amount = PERCENTAGE_UNIT_AMOUNT * (1.0 - mPost[i]);
            if (clause.isSatisified(assignment)) {
                correction += amount;
            } else {
                correction -= amount;
            }
        }
        mPercentageBonus = correction;

    }

    /**
     *
     * @return number of satisfied clauses
     */
    public int getNumberOfSatisfied() {
        return mNumberOfSatisfied;
    }

    /**
     *
     * @return whether formula is satisfied
     */
    public boolean isSatisfied() {
        return mIsSatisfied;
    }

    /**
     *
     * @return calculated percentage bonus based on formula given in lab assignment
     */
    public double getPercentageBonus() {
        return mPercentageBonus;
    }

    /**
     *
     * @return percentage for given clause
     */
    public double getPercentage(int index) {
        return mPost[index];
    }

    /**
     *
     * @return number of clauses (used for printing stats)
     */
    public int getNumberOfClauses() {
        return mPost.length;
    }
}
