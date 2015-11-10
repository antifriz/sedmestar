package hr.fer.zemris.fuzzy.controller;

/**
 * Created by ivan on 11/9/15.
 */
public class RudderFuzzySystemMin extends FuzzySystem {
    public RudderFuzzySystemMin(COADefuzzifier defuzzifier) {
        super(defuzzifier);
    }

    @Override
    public int infer(int... input) {
        return 0;
    }
}
