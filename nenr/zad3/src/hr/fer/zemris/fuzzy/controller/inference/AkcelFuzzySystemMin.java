package hr.fer.zemris.fuzzy.controller.inference;

import hr.fer.zemris.fuzzy.controller.Defuzzifier;
import hr.fer.zemris.fuzzy.sets.*;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public final class AkcelFuzzySystemMin extends FuzzySystem {
    private static final int MAX_ACCELERATION = 2;

    private final HashMap<DomainElement, IFuzzySet> mAccelerationRules;

    public AkcelFuzzySystemMin(Defuzzifier defuzzifier) {
        super(defuzzifier);
        SimpleDomain accelerationDomain = Domain.intRange(-MAX_ACCELERATION, MAX_ACCELERATION + 1);

        DomainElement minAcceleration = DomainElement.of(-MAX_ACCELERATION / 2);
        DomainElement maxAcceleration = DomainElement.of(MAX_ACCELERATION / 2);
        DomainElement zero = DomainElement.of(0);

        IFuzzySet prettyNegativeAcceleration = StandardFuzzySets.lFunctionSet(accelerationDomain, minAcceleration, zero);
        IFuzzySet aroundZeroAcceleration = StandardFuzzySets.lambdaFunctionSet(accelerationDomain, minAcceleration, zero, maxAcceleration);
        IFuzzySet prettyPositiveAcceleration = StandardFuzzySets.gammaFunctionSet(accelerationDomain, zero, maxAcceleration);

        mAccelerationRules = new HashMap<>();

        addCartesianRule(mAccelerationRules, RelativeDistance.NEGATIVE, Velocity.FAST, prettyNegativeAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.NEGATIVE, Velocity.MID, prettyNegativeAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.NEGATIVE, Velocity.SLOW, prettyPositiveAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.ZERO, Velocity.FAST, prettyNegativeAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.ZERO, Velocity.MID, aroundZeroAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.ZERO, Velocity.SLOW, prettyPositiveAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.POSITIVE, Velocity.FAST, prettyNegativeAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.POSITIVE, Velocity.MID, prettyNegativeAcceleration);
        addCartesianRule(mAccelerationRules, RelativeDistance.POSITIVE, Velocity.SLOW, prettyPositiveAcceleration);


    }

    @Override
    public int infer(int L, int D, int LK, int DK, int V, int S) {
        double k1 = 1;
        double k2 = 2;
        int input = (int) (k1 * (L - D) + (LK * Math.sqrt(2) - L) + (D - DK * Math.sqrt(2)) + k2 * (LK - DK));
        input /= 10;

        IFuzzySet LDvelocity = cartesianSet(generateFuzzyInput(input, mPrettyNegativeRelativeDistance, mAroundZeroRelativeDistance, mPrettyPositiveRelativeDistance), generateFuzzyInput(V, mSlow, mMid, mFast));

        IFuzzySet inferAcc = infer(LDvelocity, mAccelerationRules);

        return 20 * inferAcc.getDomain().elementForIndex(mDefuzzifier.defuzzify(inferAcc)).getComponentValue(0);
    }

}
