package hr.fer.zemris.nenr.zad6;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int RULE_CNT = 1;
    private static final double ETA = 0.000001;

    private static final double DESIRED_ERROR = -10e-2;
    private static final boolean STOHASTIC = true;
    public static final int ITER_COUNT = 200000;

    static class Input {
        double x;
        double y;

        public Input(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Rule {
        private static final double INIT = 0;
        double a, b, c, d, p, q, r;

        public Rule(Random random) {
            this.a = random.nextDouble() * 2 * INIT - 1;
            this.b = random.nextDouble() * 2 * INIT - 1;
            this.c = random.nextDouble() * 2 * INIT - 1;
            this.d = random.nextDouble() * 2 * INIT - 1;
            this.p = random.nextDouble() * 2 * INIT - 1;
            this.q = random.nextDouble() * 2 * INIT - 1;
            this.r = random.nextDouble() * 2 * INIT - 1;
        }

        public Rule() {
            this.a = 0;
            this.b = 0;
            this.c = 0;
            this.d = 0;
            this.p = 0;
            this.q = 0;
            this.r = 0;
        }

        public void add(Rule other) {
            a += other.a;
            b += other.b;
            c += other.c;
            d += other.d;
            p += other.p;
            q += other.q;
            r += other.r;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    ", p=" + p +
                    ", q=" + q +
                    ", r=" + r +
                    '}';
        }

        public void addDivided(Rule other, double d) {
            a += other.a / d;
            b += other.b / d;
            c += other.c / d;
            d += other.d / d;
            p += other.p / d;
            q += other.q / d;
            r += other.r / d;
            other.a = 0;
            other.b = 0;
            other.c = 0;
            other.d = 0;
            other.p = 0;
            other.q = 0;
            other.r = 0;
        }
    }

    public static void main(String[] args) {
        // write your code here

        Collection<Pair<Input, Double>> trainSamples = new ArrayList<>();
        IntStream.rangeClosed(-4, 4).forEach(x -> IntStream.rangeClosed(-4, 4).forEach(y -> {
            trainSamples.add(new Pair<>(new Input(x, y), realOutput(x, y)));
        }));

        final Random random = new Random();
        List<Rule> rules = IntStream.range(0, RULE_CNT).mapToObj(i -> new Rule(random)).collect(Collectors.toList());
        double eta = ETA;
        List<Rule> fakeRules;
        if (!STOHASTIC) {
            fakeRules = rules.stream().map(r -> new Rule()).collect(Collectors.toList());
        }

        int n = trainSamples.size();
        double[] alphas = new double[n];
        double[] betas = new double[n];
        double[] gammas = new double[n];
        double[] zeds = new double[n];

        for (int ii = 0; ii <= ITER_COUNT; ii++) {
            double err = 0;

            for (Pair<Input, Double> trainSample : trainSamples) {

                Input input = trainSample.getKey();
                double x = input.x;
                double y = input.y;
                double yReal = trainSample.getValue();
                double upper = 0, gammaSum = 0;


                for (int i = 0; i < rules.size(); i++) {
                    Rule rule = rules.get(i);
                    double alpha = membershipFunction(rule.a, rule.b, x);
                    double beta = membershipFunction(rule.c, rule.d, y);
                    alphas[i] = alpha;
                    betas[i] = beta;
                    double gamma = alpha * beta;
                    gammas[i] = gamma;
                    double z = calculateZ(input, rule);
                    zeds[i] = z;
                    upper += gamma * z;
                    gammaSum += gamma;
                }
                double o = upper / gammaSum;
                double yDiff = yReal - o;
                err += yDiff * yDiff;

                for (int i = 0; i < rules.size(); i++) {

                    double zi = zeds[i];
                    double sum_big1 = 0;
                    for (int j = 0; j < rules.size(); j++) {
                        sum_big1 += gammas[j] * (zi - calculateZ(input,rules.get(j)));
                    }
                    double sum_big = sum_big1;

                    double dr = eta * yDiff * gammas[i] / gammaSum;
                    double abcdSame = dr * sum_big / gammaSum;

                    double oneMinusAlpha = 1 - alphas[i];
                    double oneMinusBeta = 1 - betas[i];

                    Rule rule = rules.get(i);

                    update(STOHASTIC ? rule : fakeRules.get(i),
                            abcdSame * oneMinusAlpha * rule.b,
                            abcdSame * oneMinusAlpha * (rule.a - x),
                            abcdSame * oneMinusBeta * rule.d,
                            abcdSame * oneMinusBeta * (rule.c - y),
                            dr * x,
                            dr * y,
                            dr);

                }
                if (!STOHASTIC) {
                    for (int i = 0; i < rules.size(); i++) {
                        rules.get(i).addDivided(fakeRules.get(i), n);

                    }
                }
            }
            err /= n;
            if (ii % 10000 == 0 || err <= DESIRED_ERROR) {

                System.out.printf("[%7d] %6.4f\n", ii, err);
                if (err <= DESIRED_ERROR) {
                    break;
                }
            }
        }
        rules.forEach(System.out::println);

    }

    private static void update(Rule fakeRule, double da, double db, double dc, double dd, double dp, double dq, double dr) {
        fakeRule.p += dp;
        fakeRule.q += dq;
        fakeRule.r += dr;

        fakeRule.a += da;
        fakeRule.b += db;
        fakeRule.c += dc;
        fakeRule.d += dd;
    }


    private static double calcualteError(Collection<Pair<Input, Double>> collection, Collection<Rule> rules) {
        return collection.stream().mapToDouble(item -> calculateError(item, rules)).sum() / collection.size();
    }

    private static double calculateError(Pair<Input, Double> inputOutputPair, Collection<Rule> rules) {
        return calculateError(inputOutputPair.getKey(), inputOutputPair.getValue(), rules);
    }

    private static double calculateError(Input input, double desiredOutput, Collection<Rule> rules) {
        double output = calculateOutput(input, rules);
//     System.out.printf("(%6.4f,%6.4f) -> (%6.4f) %6.4f Err: %6.4f\n", input.x, input.y, desiredOutput, output, (output - desiredOutput) * (output - desiredOutput));
        return (output - desiredOutput) * (output - desiredOutput);
    }

    private static double calculateOutput(Input input, Collection<Rule> rules) {
        double upper = 0, lower = 0;
        for (Rule rule : rules) {
            double gamma = calculateGamma(input, rule);
            double z = calculateZ(input, rule);
            upper += gamma * z;
            lower += gamma;
        }
        return upper / lower;
    }


    private static double calculateGamma(Input input, Rule rule) {
        double alpha = membershipFunction(rule.a, rule.b, input.x);
        double beta = membershipFunction(rule.c, rule.d, input.y);
        double gamma = alpha * beta;
        return gamma;
    }

    private static double membershipFunction(double a, double b, double x) {
        return 1 / (1 + Math.exp(b * (x - a)));
    }

    private static double calculateZ(Input input, Rule rule) {
        return rule.p * input.x + rule.q * input.y + rule.r;
    }


    private static double realOutput(double x, double y) {
        double cos = Math.cos(x / 5);
        return ((x - 1) * (x - 1) + (y + 2) * (y + 2) - 5 * x * y + 3) * cos * cos;
//        return  x +2* y;
    }
}
