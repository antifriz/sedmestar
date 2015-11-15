package hr.fer.zemris.fuzzy.controller.inference;

import hr.fer.zemris.fuzzy.sets.Debug;
import hr.fer.zemris.fuzzy.controller.Defuzzifier;
import hr.fer.zemris.fuzzy.sets.*;

import java.util.HashMap;

/**
 * Created by ivan on 11/9/15.
 */
public final class RudderFuzzySystemMin extends FuzzySystem {
    private static final int MAX_ANGLE = 135;

    private final HashMap<DomainElement, IFuzzySet> mAngleRules;

    public RudderFuzzySystemMin(Defuzzifier defuzzifier) {
        super(defuzzifier);
        SimpleDomain angleDomain = Domain.intRange(-MAX_ANGLE, MAX_ANGLE + 1);

        DomainElement minAngle = DomainElement.of(-MAX_ANGLE);
        DomainElement maxAngle = DomainElement.of(MAX_ANGLE);
        DomainElement zero = DomainElement.of(0);

        IFuzzySet prettyNegativeAngle = StandardFuzzySets.lFunctionSet(angleDomain, minAngle, zero);
        IFuzzySet aroundZeroAngle = StandardFuzzySets.lambdaFunctionSet(angleDomain, minAngle, zero, maxAngle);
        IFuzzySet prettyPositiveAngle = StandardFuzzySets.gammaFunctionSet(angleDomain, zero, maxAngle);

        mAngleRules = new HashMap<>();

        addCartesianRule(mAngleRules, RelativeDistance.NEGATIVE, Velocity.FAST, prettyNegativeAngle);
        addCartesianRule(mAngleRules, RelativeDistance.NEGATIVE, Velocity.MID, prettyNegativeAngle);
        addCartesianRule(mAngleRules, RelativeDistance.NEGATIVE, Velocity.SLOW, prettyNegativeAngle);
        addCartesianRule(mAngleRules, RelativeDistance.ZERO, Velocity.FAST, aroundZeroAngle);
        addCartesianRule(mAngleRules, RelativeDistance.ZERO, Velocity.MID, aroundZeroAngle);
        addCartesianRule(mAngleRules, RelativeDistance.ZERO, Velocity.SLOW, aroundZeroAngle);
        addCartesianRule(mAngleRules, RelativeDistance.POSITIVE, Velocity.SLOW, prettyPositiveAngle);
        addCartesianRule(mAngleRules, RelativeDistance.POSITIVE, Velocity.MID, prettyPositiveAngle);
        addCartesianRule(mAngleRules, RelativeDistance.POSITIVE, Velocity.FAST, prettyPositiveAngle);


    }

    @Override
    public int infer(IFuzzySet LDvelocity) {
        IFuzzySet inferAngle = infer(LDvelocity, mAngleRules, verbose);

        if(verbose) Debug.print(inferAngle, "Infered angle");

        return inferAngle.getDomain().elementForIndex(mDefuzzifier.defuzzify(inferAngle)).getComponentValue(0);
    }
}
