package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.*;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public abstract class FuzzySystem {
    COADefuzzifier mDefuzzifier;

    public FuzzySystem(COADefuzzifier defuzzifier) {
        mDefuzzifier = defuzzifier;
    }

    public abstract int infer(int... input);


    public static final int DISTANCE_SPEEDUP = 10;

    public static final int MAX_ACCELERATION = 2;
    public static final int MAX_DISTANCE = 8000/DISTANCE_SPEEDUP;
    public static final int MAX_SPEED = 2000;
    private static final int MAX_ANGLE = 135;

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

    public static Pair<Integer, Integer> infer(int L, int D, int LK, int DK, int V, int S) {
        SimpleDomain relativeDistanceDomain = Domain.intRange(-MAX_DISTANCE, MAX_DISTANCE + 1);

        SimpleDomain accelerationDomain = Domain.intRange(-MAX_ACCELERATION, MAX_ACCELERATION + 1);
        SimpleDomain angleDomain = Domain.intRange(-MAX_ANGLE, MAX_ANGLE + 1);
        SimpleDomain velocityDomain = Domain.intRange(0, MAX_SPEED);

        DomainElement neg15 = DomainElement.of(-140/DISTANCE_SPEEDUP);
        DomainElement pos15 = DomainElement.of(140/DISTANCE_SPEEDUP);

        DomainElement minAcceleration = DomainElement.of(-MAX_ACCELERATION/2);
        DomainElement maxAcceleration = DomainElement.of(MAX_ACCELERATION/2);
        DomainElement minAngle = DomainElement.of(-MAX_ANGLE);
        DomainElement maxAngle = DomainElement.of(MAX_ANGLE);


        DomainElement zero = DomainElement.of(0);
        IFuzzySet prettyNegativeRelativeDistance = StandardFuzzySets.lFunctionSet(relativeDistanceDomain, neg15, zero);
        IFuzzySet aroundZeroRelativeDistance = StandardFuzzySets.lambdaFunctionSet(relativeDistanceDomain, neg15, zero, pos15);
        IFuzzySet prettyPositiveRelativeDistance = StandardFuzzySets.gammaFunctionSet(relativeDistanceDomain, zero, pos15);

        IFuzzySet prettyNegativeAcceleration = StandardFuzzySets.lFunctionSet(accelerationDomain, minAcceleration, zero);
        IFuzzySet aroundZeroAcceleration = StandardFuzzySets.lambdaFunctionSet(accelerationDomain, minAcceleration, zero, maxAcceleration);
        IFuzzySet prettyPositiveAcceleration = StandardFuzzySets.gammaFunctionSet(accelerationDomain, zero, maxAcceleration);

        IFuzzySet prettyNegativeAngle = StandardFuzzySets.lFunctionSet(angleDomain, minAngle, zero);
        IFuzzySet aroundZeroAngle = StandardFuzzySets.lambdaFunctionSet(angleDomain, minAngle, zero, maxAngle);
        IFuzzySet prettyPositiveAngle = StandardFuzzySets.gammaFunctionSet(angleDomain, zero, maxAngle);


        DomainElement velSlow = DomainElement.of(115);
        DomainElement velMid = DomainElement.of(260);
        DomainElement velHigh = DomainElement.of(270);

        IFuzzySet slow = StandardFuzzySets.lFunctionSet(velocityDomain, velSlow, velMid);
        IFuzzySet mid = StandardFuzzySets.lambdaFunctionSet(velocityDomain, velSlow, velMid, velHigh);
        IFuzzySet fast = StandardFuzzySets.gammaFunctionSet(velocityDomain, velMid, velHigh);


        double k1 =1;
        double k2 =2;
        int input = (int) (k1 * (L - D) + (LK * Math.sqrt(2) - L) + (D - DK * Math.sqrt(2)) + k2 * (LK - DK));
        input /=10;
        IFuzzySet relativeLD = generateFuzzyInput(input, prettyNegativeRelativeDistance, aroundZeroRelativeDistance, prettyPositiveRelativeDistance);
        IFuzzySet velocity = generateFuzzyInput(V, slow, mid, fast);

        IFuzzySet LDvelocity = cartesianSet(relativeLD, velocity);

        HashMap<DomainElement, IFuzzySet> accelerationRules = new HashMap<>();

        addCartesianRule(accelerationRules, RelativeDistance.NEGATIVE, Velocity.FAST, prettyNegativeAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.NEGATIVE, Velocity.MID, prettyNegativeAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.NEGATIVE, Velocity.SLOW, prettyPositiveAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.ZERO, Velocity.FAST, prettyNegativeAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.ZERO, Velocity.MID, aroundZeroAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.ZERO, Velocity.SLOW, prettyPositiveAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.POSITIVE, Velocity.FAST, prettyNegativeAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.POSITIVE, Velocity.MID, prettyNegativeAcceleration);
        addCartesianRule(accelerationRules, RelativeDistance.POSITIVE, Velocity.SLOW, prettyPositiveAcceleration);

        HashMap<DomainElement, IFuzzySet> angleRules = new HashMap<>();

        addCartesianRule(angleRules, RelativeDistance.NEGATIVE, Velocity.FAST, prettyNegativeAngle);
        addCartesianRule(angleRules, RelativeDistance.NEGATIVE, Velocity.MID, prettyNegativeAngle);
        addCartesianRule(angleRules, RelativeDistance.NEGATIVE, Velocity.SLOW, prettyNegativeAngle);
        addCartesianRule(angleRules, RelativeDistance.ZERO, Velocity.FAST, aroundZeroAngle);
        addCartesianRule(angleRules, RelativeDistance.ZERO, Velocity.MID, aroundZeroAngle);
        addCartesianRule(angleRules, RelativeDistance.ZERO, Velocity.SLOW, aroundZeroAngle);
        addCartesianRule(angleRules, RelativeDistance.POSITIVE, Velocity.SLOW, prettyPositiveAngle);
        addCartesianRule(angleRules, RelativeDistance.POSITIVE, Velocity.MID, prettyPositiveAngle);
        addCartesianRule(angleRules, RelativeDistance.POSITIVE, Velocity.FAST, prettyPositiveAngle);


        IFuzzySet inferAcc = infer(LDvelocity, accelerationRules);

        int x1 = COADefuzzifier.indexOfDefuzzified(inferAcc);
        int acceleration = inferAcc.getDomain().elementForIndex(x1).getComponentValue(0);
        IFuzzySet inferAngle = infer(LDvelocity, angleRules);

        int x2 = COADefuzzifier.indexOfDefuzzified(inferAngle);
        int angle = inferAngle.getDomain().elementForIndex(x2).getComponentValue(0);

        return new Pair<>(acceleration*20, angle);
    }


    private static IFuzzySet infer(IFuzzySet input, HashMap<DomainElement, IFuzzySet> rules) {

        IFuzzySet output = new MutableFuzzySet(rules.entrySet().iterator().next().getValue().getDomain());
        for (DomainElement element : input.getDomain()) {
            IFuzzySet set = Operations.unaryOperation(rules.get(element), domainValue -> Operations.zadehAnd().valueAt(domainValue, input.getValueAt(element)));

            output = Operations.binaryOperation(output, set, Operations.zadehOr());
        }
        return output;
    }

    private static IFuzzySet cartesianSet(final IFuzzySet first, final IFuzzySet second) {
        Domain combine = Domain.combine(first.getDomain(), second.getDomain());
        return new CalculatedFuzzySet(combine, x -> {
            DomainElement domainElement = combine.elementForIndex(x);
            return Operations.zadehAnd().valueAt(
                    first.getValueAt(domainElement.getComponentValue(0)),
                    second.getValueAt(domainElement.getComponentValue(1)));
        });
    }

    private static <T extends Enum<T>, U extends Enum<U>> void addCartesianRule(HashMap<DomainElement, IFuzzySet> rules, T antecedent1, U antecedent2, IFuzzySet consequent) {
        rules.put(DomainElement.of(antecedent1.ordinal(), antecedent2.ordinal()), consequent);
    }

    private static <T extends Enum<T>> void addRule(HashMap<DomainElement, IFuzzySet> rules, T antecedent, IFuzzySet consequent) {
        rules.put(DomainElement.of(antecedent.ordinal()), consequent);
    }

    private static IFuzzySet generateFuzzyInput(int input, IFuzzySet... sets) {
        return new CalculatedFuzzySet(Domain.intRange(0, sets.length), x -> {
            //Debug.print(sets[x],">"+x);
            return sets[x].getValueAt(input);
        });
    }

}
