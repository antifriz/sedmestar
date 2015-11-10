package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.CalculatedFuzzySet;
import hr.fer.zemris.fuzzy.sets.IDomain;
import hr.fer.zemris.fuzzy.sets.IFuzzySet;
import hr.fer.zemris.fuzzy.sets.IIntUnaryFunction;

/**
 * Created by ivan on 11/9/15.
 */
public class FuzzyInference extends CalculatedFuzzySet {
    public FuzzyInference(IDomain domain, IIntUnaryFunction function) {
        super(domain, function);
    }
}
