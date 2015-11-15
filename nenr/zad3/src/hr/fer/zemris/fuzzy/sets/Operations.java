package hr.fer.zemris.fuzzy.sets;

/**
 * Created by ivan on 10/17/15.
 */
public class Operations {
    public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction function) {
        IDomain newDomain = set.getDomain();
        return new CalculatedFuzzySet(newDomain, x -> function.valueAt(set.getValueAt(newDomain.elementForIndex(x))));
    }

    public static IFuzzySet binaryOperation(IFuzzySet setA, IFuzzySet setB, IBinaryFunction function) {
        assert setA.getDomain().equals(setB.getDomain());
        return new CalculatedFuzzySet(setA.getDomain(), x -> function.valueAt(
                setA.getValueAt(
                        setA.getDomain().elementForIndex(x)),
                setB.getValueAt(
                        setB.getDomain().elementForIndex(x)
                )));
    }

    public static IUnaryFunction zadehNot() {
        return domainValue -> 1 - domainValue;
    }

    public static IBinaryFunction zadehAnd() {
        return Math::min;
    }

    public static IBinaryFunction zadehOr() {
        return Math::max;
    }

}
