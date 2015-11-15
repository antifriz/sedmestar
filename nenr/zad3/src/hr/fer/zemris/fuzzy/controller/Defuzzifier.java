package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.IFuzzySet;

/**
 * Created by ivan on 11/15/15.
 */
public interface Defuzzifier {
    int defuzzify(IFuzzySet set);
}
