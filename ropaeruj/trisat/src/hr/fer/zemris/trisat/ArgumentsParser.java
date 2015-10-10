package hr.fer.zemris.trisat;

/**
 * Created by ivan on 10/10/15.
 */
public class ArgumentsParser {

    private Algorithm mAlgorithm;
    private SATFormula mSatFormula;

    private ArgumentsParser() {
    }

    public static ArgumentsParser createArgumentsParser() {
        return new ArgumentsParser();
    }

    public boolean parseArguments(String[] args){
        if(args.length!=2){
            return false;
        }
        mAlgorithm = AlgorithmFactory.createFromTag(args[0]);
        if(mAlgorithm==null){
            return false;
        }
        mSatFormula = SATFormulaFactory.createFromFile(args[1]);
        return mSatFormula != null;
    }

    public Algorithm getAlgorithm(){
        return mAlgorithm;
    }

    public SATFormula getSATFormula() {
        return mSatFormula;
    }
}
