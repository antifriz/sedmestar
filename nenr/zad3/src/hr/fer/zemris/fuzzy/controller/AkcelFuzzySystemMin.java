package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.*;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public class AkcelFuzzySystemMin extends FuzzySystem {

    public static final int DISTANCE_SPEEDUP = 10;

    public static final int MAX_ACCELERATION = 2;
    public static final int MAX_DISTANCE = 8000/DISTANCE_SPEEDUP;
    public static final int MAX_SPEED = 2000;

    public AkcelFuzzySystemMin(Defuzzifier defuzzifier) {
        super(defuzzifier);
    }

    @Override
    public int infer(int L, int D, int LK, int DK, int V, int S) {
        SimpleDomain relativeDistanceDomain = Domain.intRange(-MAX_DISTANCE, MAX_DISTANCE + 1);

        SimpleDomain accelerationDomain = Domain.intRange(-MAX_ACCELERATION, MAX_ACCELERATION + 1);
        SimpleDomain velocityDomain = Domain.intRange(0, MAX_SPEED);

        DomainElement neg15 = DomainElement.of(-140 / DISTANCE_SPEEDUP);
        DomainElement pos15 = DomainElement.of(140 / DISTANCE_SPEEDUP);

        DomainElement minAcceleration = DomainElement.of(-MAX_ACCELERATION / 2);
        DomainElement maxAcceleration = DomainElement.of(MAX_ACCELERATION / 2);


        DomainElement zero = DomainElement.of(0);
        IFuzzySet prettyNegativeRelativeDistance = StandardFuzzySets.lFunctionSet(relativeDistanceDomain, neg15, zero);
        IFuzzySet aroundZeroRelativeDistance = StandardFuzzySets.lambdaFunctionSet(relativeDistanceDomain, neg15, zero, pos15);
        IFuzzySet prettyPositiveRelativeDistance = StandardFuzzySets.gammaFunctionSet(relativeDistanceDomain, zero, pos15);

        IFuzzySet prettyNegativeAcceleration = StandardFuzzySets.lFunctionSet(accelerationDomain, minAcceleration, zero);
        IFuzzySet aroundZeroAcceleration = StandardFuzzySets.lambdaFunctionSet(accelerationDomain, minAcceleration, zero, maxAcceleration);
        IFuzzySet prettyPositiveAcceleration = StandardFuzzySets.gammaFunctionSet(accelerationDomain, zero, maxAcceleration);


        DomainElement velSlow = DomainElement.of(115);
        DomainElement velMid = DomainElement.of(260);
        DomainElement velHigh = DomainElement.of(270);

        IFuzzySet slow = StandardFuzzySets.lFunctionSet(velocityDomain, velSlow, velMid);
        IFuzzySet mid = StandardFuzzySets.lambdaFunctionSet(velocityDomain, velSlow, velMid, velHigh);
        IFuzzySet fast = StandardFuzzySets.gammaFunctionSet(velocityDomain, velMid, velHigh);


        double k1 = 1;
        double k2 = 2;
        int input = (int) (k1 * (L - D) + (LK * Math.sqrt(2) - L) + (D - DK * Math.sqrt(2)) + k2 * (LK - DK));
        input /= 10;
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

        IFuzzySet inferAcc = infer(LDvelocity, accelerationRules);

        int x1 = mDefuzzifier.defuzzify(inferAcc);
        int acceleration = inferAcc.getDomain().elementForIndex(x1).getComponentValue(0);

        return acceleration * 20;
    }

}
