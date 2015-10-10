package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 */
public class Algorithm1 implements Algorithm {

    private SATFormula mSATFormula;
    private SATProblemResults mSATProblemResults;
    private MutableBitVector mMutableBitVector;

    @Override
    public SATProblemResults solveSATProblem(SATFormula formula) {
        mSATFormula = formula;
        mSATProblemResults = new SATProblemResults();

        mMutableBitVector= new MutableBitVector(formula.getNumberOfVariables());
        run(formula.getNumberOfVariables());

        return mSATProblemResults;
    }

    private void run(int n) {
        if (n > 0) {
            int n1 = n - 1;
            run(n1);
            mMutableBitVector.toggle(n1);
            run(n1);
            mMutableBitVector.toggle(n1);
        } else {
            if(mSATFormula.isSatisfied(mMutableBitVector)){
                mSATProblemResults.addResult(mMutableBitVector);

            }
        }
    }
}
