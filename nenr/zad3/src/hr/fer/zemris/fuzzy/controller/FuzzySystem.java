package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.*;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public abstract class FuzzySystem {
    Defuzzifier mDefuzzifier;

    public FuzzySystem(Defuzzifier defuzzifier) {
        mDefuzzifier = defuzzifier;
    }

    public abstract int infer(int L, int D, int LK, int DK, int V, int S);

    enum RelativeDistance {
        NEGATIVE,
        ZERO,
        POSITIVE
    }

    enum Velocity {
        SLOW,
        MID,
        FAST
    }


    static IFuzzySet infer(IFuzzySet input, HashMap<DomainElement, IFuzzySet> rules) {

        IFuzzySet output = new MutableFuzzySet(rules.entrySet().iterator().next().getValue().getDomain());
        for (DomainElement element : input.getDomain()) {
            IFuzzySet set = Operations.unaryOperation(rules.get(element), domainValue -> Operations.zadehAnd().valueAt(domainValue, input.getValueAt(element)));

            output = Operations.binaryOperation(output, set, Operations.zadehOr());
        }
        return output;
    }

    static IFuzzySet cartesianSet(final IFuzzySet first, final IFuzzySet second) {
        Domain combine = Domain.combine(first.getDomain(), second.getDomain());
        return new CalculatedFuzzySet(combine, x -> {
            DomainElement domainElement = combine.elementForIndex(x);
            return Operations.zadehAnd().valueAt(
                    first.getValueAt(domainElement.getComponentValue(0)),
                    second.getValueAt(domainElement.getComponentValue(1)));
        });
    }

    static <T extends Enum<T>, U extends Enum<U>> void addCartesianRule(HashMap<DomainElement, IFuzzySet> rules, T antecedent1, U antecedent2, IFuzzySet consequent) {
        rules.put(DomainElement.of(antecedent1.ordinal(), antecedent2.ordinal()), consequent);
    }
//
//    static <T extends Enum<T>> void addRule(HashMap<DomainElement, IFuzzySet> rules, T antecedent, IFuzzySet consequent) {
//        rules.put(DomainElement.of(antecedent.ordinal()), consequent);
//    }

    static IFuzzySet generateFuzzyInput(int input, IFuzzySet... sets) {
        return new CalculatedFuzzySet(Domain.intRange(0, sets.length), x -> sets[x].getValueAt(input));
    }

}
