package hr.fer.zemris.fuzzy.controller;

import hr.fer.zemris.fuzzy.sets.*;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public class RudderFuzzySystemMin extends FuzzySystem {
    public RudderFuzzySystemMin(Defuzzifier defuzzifier) {
        super(defuzzifier);
    }

    public static final int DISTANCE_SPEEDUP = 10;

    public static final int MAX_DISTANCE = 8000 / DISTANCE_SPEEDUP;
    public static final int MAX_SPEED = 2000;
    private static final int MAX_ANGLE = 135;

    @Override
    public int infer(int L, int D, int LK, int DK, int V, int S) {
        SimpleDomain relativeDistanceDomain = Domain.intRange(-MAX_DISTANCE, MAX_DISTANCE + 1);

        SimpleDomain angleDomain = Domain.intRange(-MAX_ANGLE, MAX_ANGLE + 1);
        SimpleDomain velocityDomain = Domain.intRange(0, MAX_SPEED);

        DomainElement neg15 = DomainElement.of(-140 / DISTANCE_SPEEDUP);
        DomainElement pos15 = DomainElement.of(140 / DISTANCE_SPEEDUP);

        DomainElement minAngle = DomainElement.of(-MAX_ANGLE);
        DomainElement maxAngle = DomainElement.of(MAX_ANGLE);


        DomainElement zero = DomainElement.of(0);
        IFuzzySet prettyNegativeRelativeDistance = StandardFuzzySets.lFunctionSet(relativeDistanceDomain, neg15, zero);
        IFuzzySet aroundZeroRelativeDistance = StandardFuzzySets.lambdaFunctionSet(relativeDistanceDomain, neg15, zero, pos15);
        IFuzzySet prettyPositiveRelativeDistance = StandardFuzzySets.gammaFunctionSet(relativeDistanceDomain, zero, pos15);

        IFuzzySet prettyNegativeAngle = StandardFuzzySets.lFunctionSet(angleDomain, minAngle, zero);
        IFuzzySet aroundZeroAngle = StandardFuzzySets.lambdaFunctionSet(angleDomain, minAngle, zero, maxAngle);
        IFuzzySet prettyPositiveAngle = StandardFuzzySets.gammaFunctionSet(angleDomain, zero, maxAngle);


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


        IFuzzySet inferAngle = infer(LDvelocity, angleRules);

        int x2 = mDefuzzifier.defuzzify(inferAngle);
        int angle = inferAngle.getDomain().elementForIndex(x2).getComponentValue(0);

        return angle;
    }

}
