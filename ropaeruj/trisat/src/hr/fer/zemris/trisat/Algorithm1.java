package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 */
public class Algorithm1 implements Algorithm {

    private SATFormula mSATFormula;
    private MutableBitVector mMutableBitVector;
    private boolean mHasFoundResult;

    @Override
    public void solveSATProblem(SATFormula formula) {
        mSATFormula = formula;

        mMutableBitVector= new MutableBitVector(formula.getNumberOfVariables());

        mHasFoundResult = false;
        run(formula.getNumberOfVariables());

        if(!mHasFoundResult){
            System.out.println("Dokazivanje nije uspjelo");
        }
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
                System.out.printf("Zadovoljivo: %s\n",mMutableBitVector);
            }
        }
    }
}
