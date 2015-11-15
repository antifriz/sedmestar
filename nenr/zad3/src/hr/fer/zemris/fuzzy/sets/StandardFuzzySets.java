package hr.fer.zemris.fuzzy.sets;

/**
 * Created by ivan on 10/15/15.
 */
public class StandardFuzzySets {
    public static IFuzzySet lFunctionSet(IDomain domain, DomainElement from, DomainElement to) {
        return new CalculatedFuzzySet(domain, StandardFuzzySets.lFunction(domain.indexOfElement(from), domain.indexOfElement(to)));
    }

    public static IFuzzySet lambdaFunctionSet(IDomain domain, DomainElement from, DomainElement peak, DomainElement to) {
        return new CalculatedFuzzySet(domain, StandardFuzzySets.lambdaFunction(domain.indexOfElement(from), domain.indexOfElement(peak), domain.indexOfElement(to)));
    }

    public static IFuzzySet gammaFunctionSet(IDomain domain, DomainElement from, DomainElement to) {
        return new CalculatedFuzzySet(domain, StandardFuzzySets.gammaFunction(domain.indexOfElement(from), domain.indexOfElement(to)));
    }

    private static IIntUnaryFunction lFunction(int from, int to) {
        if (from > to) {
            throw new IllegalStateException();
        }
        return x -> {
            if (x < from) {
                return 1;
            }
            if (x < to) {
                return (to - x) / (double) (to - from);
            }
            return 0;
        };
    }

    private static IIntUnaryFunction gammaFunction(int from, int to) {
        if (from > to) {
            throw new IllegalStateException();
        }
        return x -> {
            if (x < from) {
                return 0;
            }
            if (x < to) {
                return (x - from) / (double) (to - from);
            }
            return 1;
        };
    }

    private static IIntUnaryFunction lambdaFunction(int from, int peak, int to) {
        if (from > peak || peak > to) {
            throw new IllegalStateException();
        }
        return x -> {
            if (x < from) {
                return 0;
            }
            if (x < peak) {
                return (x - from) / (double) (to - peak);
            }
            if (x < to) {
                return (to - x) / (double) (to - peak);
            }
            return 0;
        };
    }

}
