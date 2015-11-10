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



    public static final int MAX_ACCELERATION = 10;
    public static final int MAX_DISTANCE = 1200;
    public static final int MAX_SPEED = 1000;
    private static final int MAX_ANGLE = 60;

    enum RelativeDistance {
        NEGATIVE,
        ZERO,
        POSITIVE
    }

    enum Velocity {
        SLOW,
        FAST
    }

    enum Acceleration {
        NEGATIVE,
        ZERO,
        POSITIVE
    }

    public static Pair<Integer,Integer> infer(int LD, int LKDK, int V){
        SimpleDomain relativeDistanceDomain = Domain.intRange(-MAX_DISTANCE, MAX_DISTANCE + 1);

        SimpleDomain accelerationDomain = Domain.intRange(-MAX_ACCELERATION, MAX_ACCELERATION + 1);
        SimpleDomain angleDomain = Domain.intRange(-MAX_ANGLE, MAX_ANGLE + 1);
        SimpleDomain velocityDomain = Domain.intRange(0, MAX_SPEED);

        DomainElement neg15 = DomainElement.of(-15);
        DomainElement pos15 = DomainElement.of(15);
        DomainElement neg30 = DomainElement.of(-30);
        DomainElement pos30 = DomainElement.of(30);
        DomainElement pos45 = DomainElement.of(45);

        DomainElement minAcceleration = DomainElement.of(-MAX_ACCELERATION);
        DomainElement maxAcceleration = DomainElement.of(MAX_ACCELERATION);
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


        IFuzzySet slow = StandardFuzzySets.lFunctionSet(velocityDomain, pos15, pos45);
        IFuzzySet fast = StandardFuzzySets.gammaFunctionSet(velocityDomain, pos15, pos45);


        IFuzzySet relativeLD = generateFuzzyInput(LD, prettyNegativeRelativeDistance, aroundZeroRelativeDistance, prettyPositiveRelativeDistance);
        IFuzzySet relativeLKDK = generateFuzzyInput(LKDK, prettyNegativeRelativeDistance, aroundZeroRelativeDistance, prettyPositiveRelativeDistance);
        IFuzzySet velocity = generateFuzzyInput(V, slow, fast);


        IFuzzySet LDvelocity = cartesianSet(relativeLD, velocity);
        IFuzzySet LKDKvelocity = cartesianSet(relativeLKDK, velocity);

        HashMap<DomainElement, IFuzzySet> accelerationRules = new HashMap<>();

        addRule(accelerationRules, RelativeDistance.NEGATIVE, Velocity.FAST, prettyPositiveAcceleration);
        addRule(accelerationRules, RelativeDistance.NEGATIVE, Velocity.SLOW, prettyPositiveAcceleration);
        addRule(accelerationRules, RelativeDistance.ZERO, Velocity.FAST, prettyNegativeAcceleration);
        addRule(accelerationRules, RelativeDistance.ZERO, Velocity.SLOW, prettyPositiveAcceleration);
        addRule(accelerationRules, RelativeDistance.POSITIVE, Velocity.SLOW, prettyPositiveAcceleration);
        addRule(accelerationRules, RelativeDistance.POSITIVE, Velocity.FAST, prettyPositiveAcceleration);

        HashMap<DomainElement, IFuzzySet> angleRules = new HashMap<>();

        addRule(angleRules, RelativeDistance.NEGATIVE, Velocity.FAST, prettyNegativeAngle);
        addRule(angleRules, RelativeDistance.NEGATIVE, Velocity.SLOW, prettyNegativeAngle);
        addRule(angleRules, RelativeDistance.ZERO, Velocity.FAST, aroundZeroAngle);
        addRule(angleRules, RelativeDistance.ZERO, Velocity.SLOW, aroundZeroAngle);
        addRule(angleRules, RelativeDistance.POSITIVE, Velocity.SLOW, prettyPositiveAngle);
        addRule(angleRules, RelativeDistance.POSITIVE, Velocity.FAST, prettyPositiveAngle);


        IFuzzySet inferAccFromLKDK = infer(LKDKvelocity, accelerationRules);
        //IFuzzySet inferAccFromLD = infer(LDvelocity, accelerationRules);

        IFuzzySet inferAcc =inferAccFromLKDK;// Operations.binaryOperation(inferAccFromLKDK,inferAccFromLD,Operations.zadehOr());
        int x1 = COADefuzzifier.indexOfDefuzzified(inferAcc);
        int acceleration = inferAcc.getDomain().elementForIndex(x1).getComponentValue(0);
        //Debug.print(inferAcc,"acc");
        //System.out.println(acceleration);

        IFuzzySet inferAngleFromLKDK = infer(LKDKvelocity, angleRules);
        //IFuzzySet inferAngleFromLD = infer(LDvelocity, angleRules);

        IFuzzySet inferAngle = inferAngleFromLKDK;//Operations.binaryOperation(inferAngleFromLKDK,inferAngleFromLD,Operations.zadehOr());
        int x2 = COADefuzzifier.indexOfDefuzzified(inferAngle);
        int angle = inferAngle.getDomain().elementForIndex(x2).getComponentValue(0);
        //Debug.print(inferAngle,"angle");
        //System.out.println(angle);


        return new Pair<>(acceleration,angle);
    }


    private static IFuzzySet infer(IFuzzySet input, HashMap<DomainElement, IFuzzySet> rules) {

        IFuzzySet output =new MutableFuzzySet(rules.entrySet().iterator().next().getValue().getDomain());
        for(DomainElement element: input.getDomain()){
            IFuzzySet set = Operations.unaryOperation(rules.get(element), domainValue -> Operations.zadehAnd().valueAt(domainValue, input.getValueAt(element)));

            output = Operations.binaryOperation(output,set,Operations.zadehOr());
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

    private static <T extends Enum<T>, U extends Enum<U>> void addRule(HashMap<DomainElement, IFuzzySet> rules, T antecedent1, U antecedent2, IFuzzySet consequent) {
        rules.put(DomainElement.of(antecedent1.ordinal(), antecedent2.ordinal()), consequent);
    }

    private static IFuzzySet generateFuzzyInput(int input, IFuzzySet... sets) {
        return new CalculatedFuzzySet(Domain.intRange(0, sets.length), x -> {
            //Debug.print(sets[x],">"+x);
            return sets[x].getValueAt(input);
        });
    }

}
