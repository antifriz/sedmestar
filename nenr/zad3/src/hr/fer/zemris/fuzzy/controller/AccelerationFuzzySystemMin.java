package hr.fer.zemris.fuzzy.controller;

/**
 * Created by ivan on 11/9/15.
 */
public class AccelerationFuzzySystemMin extends FuzzySystem {
    public AccelerationFuzzySystemMin(COADefuzzifier defuzzifier) {
        super(defuzzifier);
    }

    @Override
    public int infer(int... input) {
        return 0;
    }
}
