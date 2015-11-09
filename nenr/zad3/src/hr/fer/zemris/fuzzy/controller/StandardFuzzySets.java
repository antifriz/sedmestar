package hr.fer.zemris.fuzzy.controller;

/**
 * Created by ivan on 10/15/15.
 */
public class StandardFuzzySets {
    public static IIntUnaryFunction lFunction(int from, int to) {
        if (from > to) {
            throw new IllegalStateException();
        }
        return new IIntUnaryFunction() {
            @Override
            public double valueAt(int x) {
                if (x < from) {
                    return 1;
                }
                if (x < to) {
                    return (to - x) / (double) (to - from);
                }
                return 0;
            }
        };
    }

    public static IIntUnaryFunction gammaFunction(int from, int to) {
        if (from > to) {
            throw new IllegalStateException();
        }
        return new IIntUnaryFunction() {
            @Override
            public double valueAt(int x) {
                if (x < from) {
                    return 0;
                }
                if (x < to) {
                    return (x - from) / (double) (to - from);
                }
                return 1;
            }
        };
    }

    public static IIntUnaryFunction lambdaFunction(int from, int peak, int to) {
        if (from > peak || peak > to) {
            throw new IllegalStateException();
        }
        return new IIntUnaryFunction() {
            @Override
            public double valueAt(int x) {
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
            }
        };
    }

}
