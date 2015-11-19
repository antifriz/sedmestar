package hr.fer.zemris.fuzzy.controller.inference;

import hr.fer.zemris.fuzzy.sets.Debug;
import hr.fer.zemris.fuzzy.controller.Defuzzifier;
import hr.fer.zemris.fuzzy.sets.*;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public abstract class FuzzySystem {
    final Defuzzifier mDefuzzifier;
    private static final int MAX_SPEED = 2000;
    private static final int DISTANCE_SPEEDUP = 10;

    private static final int MAX_DISTANCE = 8000 / DISTANCE_SPEEDUP;
    final IFuzzySet mSlow;
    final IFuzzySet mMid;
    final IFuzzySet mFast;
    final IFuzzySet mPrettyNegativeRelativeDistance;
    final IFuzzySet mAroundZeroRelativeDistance;
    final IFuzzySet mPrettyPositiveRelativeDistance;

    public boolean verbose = false;

    FuzzySystem(Defuzzifier defuzzifier) {
        mDefuzzifier = defuzzifier;


        DomainElement velSlow = DomainElement.of(145);
        DomainElement velMid = DomainElement.of(250);
        DomainElement velHigh = DomainElement.of(270);

        SimpleDomain velocityDomain = Domain.intRange(0, MAX_SPEED);

        mSlow = StandardFuzzySets.lFunctionSet(velocityDomain, velSlow, velMid);
        mMid = StandardFuzzySets.lambdaFunctionSet(velocityDomain, velSlow, velMid, velHigh);
        mFast = StandardFuzzySets.gammaFunctionSet(velocityDomain, velMid, velHigh);


        SimpleDomain relativeDistanceDomain = Domain.intRange(-MAX_DISTANCE, MAX_DISTANCE + 1);


        DomainElement neg15 = DomainElement.of(-200 / DISTANCE_SPEEDUP);
        DomainElement pos15 = DomainElement.of(200 / DISTANCE_SPEEDUP);
        DomainElement zero = DomainElement.of(0);

        mPrettyNegativeRelativeDistance = StandardFuzzySets.lFunctionSet(relativeDistanceDomain, neg15, zero);
        mAroundZeroRelativeDistance = StandardFuzzySets.lambdaFunctionSet(relativeDistanceDomain, neg15, zero, pos15);
        mPrettyPositiveRelativeDistance = StandardFuzzySets.gammaFunctionSet(relativeDistanceDomain, zero, pos15);


    }

    public final int infer(int L, int D, int LK, int DK, int V, int S) {
        double k1 = 2;
        double k2 = 2;
        int R = (int) (k1 * (L - D) + (LK * Math.sqrt(2) - L) + (D - DK * Math.sqrt(2)) + k2 * (LK - DK));
        R /= DISTANCE_SPEEDUP;

        IFuzzySet LDvelocity = cartesianSet(generateFuzzyInput(R, mPrettyNegativeRelativeDistance, mAroundZeroRelativeDistance, mPrettyPositiveRelativeDistance), generateFuzzyInput(V, mSlow, mMid, mFast));

        return infer(LDvelocity);
    }

    public abstract int infer(IFuzzySet LDvelocity);

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


    static IFuzzySet infer(IFuzzySet input, HashMap<DomainElement, IFuzzySet> rules, boolean verbose) {

        IFuzzySet output = new MutableFuzzySet(rules.entrySet().iterator().next().getValue().getDomain());
        for (DomainElement element : input.getDomain()) {
            // IFuzzySet set = Operations.unaryOperation(rules.get(element), domainValue -> Operations.zadehAnd().valueAt(domainValue, input.getValueAt(element)));
            IFuzzySet set = Operations.unaryOperation(rules.get(element), domainValue -> domainValue * input.getValueAt(element));

            if(verbose) Debug.print(set,element.toString());

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

    static IFuzzySet generateFuzzyInput(int input, IFuzzySet... sets) {
        return new CalculatedFuzzySet(Domain.intRange(0, sets.length), x -> sets[x].getValueAt(input));
    }

}
