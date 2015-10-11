package hr.fer.zemris.trisat;

import java.util.Arrays;

/**
 * Created by ivan on 10/10/15.
 */
public class SATFormula {
    private final int mNumberOfVariables;
    private final Clause[] mClauses;

    public SATFormula(int numberOfVariables, Clause[] clauses){
        mNumberOfVariables = numberOfVariables;
        mClauses = clauses;
    }

    public int getNumberOfVariables(){
        return mNumberOfVariables;
    }

    public int getNumberOfClauses(){
        return mClauses.length;
    }

    public Clause getClause(int index){
        return mClauses[index];
    }

    public boolean isSatisfied(BitVector assignment){
        return Arrays.stream(mClauses).allMatch(x->x.isSatisified(assignment));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Clause clause : mClauses) {
            sb.append(clause.toString());
            sb.append("\n");
        }
        if(mClauses.length>0){
            sb.delete(mClauses.length-1,mClauses.length);
        }
        return sb.toString();
    }
}
