package hr.fer.zemris.fuzzy.controller.inference;

import hr.fer.zemris.fuzzy.sets.Debug;
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
    public int infer(IFuzzySet LDvelocity) {

        IFuzzySet inferAcc = infer(LDvelocity, mAccelerationRules, verbose);

        if(verbose) Debug.print(inferAcc, "Infered acc");

        return 20 * inferAcc.getDomain().elementForIndex(mDefuzzifier.defuzzify(inferAcc)).getComponentValue(0);
    }

}
