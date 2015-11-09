package hr.fer.zemris.fuzzy.controller;

/**
 * Created by ivan on 10/17/15.
 */
public class Operations {
    public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction function) {
        IDomain newDomain = set.getDomain();
        return new CalculatedFuzzySet(newDomain, new IIntUnaryFunction() {
            @Override
            public double valueAt(int x) {
                return function.valueAt(set.getValueAt(newDomain.elementForIndex(x)));
            }
        });
    }

    public static IFuzzySet binaryOperation(IFuzzySet setA, IFuzzySet setB, IBinaryFunction function) {
        assert setA.getDomain().equals(setB.getDomain());
        return new CalculatedFuzzySet(setA.getDomain(), new IIntUnaryFunction() {

            @Override
            public double valueAt(int x) {
                return function.valueAt(
                        setA.getValueAt(
                                setA.getDomain().elementForIndex(x)),
                        setB.getValueAt(
                                setB.getDomain().elementForIndex(x)
                        ));
            }
        });
    }

    public static IUnaryFunction zadehNot() {
        return new IUnaryFunction() {
            @Override
            public double valueAt(double domainValue) {
                return 1 - domainValue;
            }
        };
    }

    public static IBinaryFunction zadehAnd() {
        return new IBinaryFunction() {
            @Override
            public double valueAt(double domainAValue, double domainBValue) {
                return Math.min(domainAValue, domainBValue);
            }
        };
    }

    public static IBinaryFunction zadehOr() {
        return new IBinaryFunction() {
            @Override
            public double valueAt(double domainAValue, double domainBValue) {
                return Math.max(domainAValue, domainBValue);
            }
        };
    }

    public static IBinaryFunction hamacherTNorm(double alpha) {
        return new IBinaryFunction() {
            @Override
            public double valueAt(double domainAValue, double domainBValue) {
                return (domainAValue * domainBValue) / (alpha + (1 - alpha) * (domainAValue + domainBValue - domainAValue * domainBValue));
            }
        };
    }

    public static IBinaryFunction hamacherSNorm(double alpha) {
        return new IBinaryFunction() {
            @Override
            public double valueAt(double domainAValue, double domainBValue) {
                return (domainAValue + domainBValue - (2 - alpha) * domainAValue * domainBValue) / (1 - (1 - alpha) * domainAValue * domainBValue);
            }
        };
    }

}
