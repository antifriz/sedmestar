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

    public static IIntUnaryFunction lFunction(int from, int to) {
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

    public static IIntUnaryFunction gammaFunction(int from, int to) {
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

    public static IIntUnaryFunction lambdaFunction(int from, int peak, int to) {
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

    public static IIntUnaryFunction concentrate(IIntUnaryFunction function) {
        return x -> Math.pow(function.valueAt(x), 2);
    }

    public static IIntUnaryFunction dilate(IIntUnaryFunction function) {
        return x -> Math.sqrt(function.valueAt(x));
    }

    public static IIntUnaryFunction contrastIntensify(IIntUnaryFunction function) {
        return x -> {
            double value = function.valueAt(x);
            return value < 0.5 ? 2 * value * value : 1 - 2 * (1 - value) * (1 - value);
        };
    }

}
