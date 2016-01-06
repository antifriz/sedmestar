package hr.fer.zemris.nenr.zad6;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int RULE_CNT = 6;
    private static final double ETA = 0.001;
    public static final int RANGE = 4;

    static class Input {
        double x;
        double y;

        public Input(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Rule {
        private static final double INIT = 1;
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
    }

    public static void main(String[] args) {
        // write your code here

        Collection<Pair<Input, Double>> trainSamples = new ArrayList<>();
        IntStream.rangeClosed(-RANGE, RANGE).forEach(x -> IntStream.rangeClosed(-RANGE, RANGE).forEach(y -> {
            trainSamples.add(new Pair<>(new Input(x, y), realOutput(x, y)));
        }));

        final Random random = new Random();
        List<Rule> rules = IntStream.range(0, RULE_CNT).mapToObj(i -> new Rule()).collect(Collectors.toList());


        while (true) {
//            System.out.println(calcualteError(trainSamples, rules));
            for (Pair<Input, Double> trainSample : trainSamples) {

                Input input = trainSample.getKey();
                double x = input.x;
                double y = input.y;
                double yReal = trainSample.getValue();
                double upper = 0, lower = 0;
                List<Double> alphas = new ArrayList<>();
                List<Double> betas = new ArrayList<>();
                List<Double> gammas = new ArrayList<>();
                List<Double> zeds = new ArrayList<>();
                for (Rule rule : rules) {
                    double alpha = membershipFunction(rule.a, rule.b, input.x);
                    double beta = membershipFunction(rule.c, rule.d, input.y);
                    alphas.add(alpha);
                    betas.add(beta);
                    double gamma = alpha * beta;
                    gammas.add(gamma);
                    double z = calculateZ(input, rule);
                    zeds.add(z);
                    upper += gamma * z;
                    lower += gamma;
                }
                if (lower == 0) {
                    x = x;
                }
                double o = upper / lower;
                System.out.println(o);
                double yDiff = yReal - o;

                double gammaSum = gammas.stream().mapToDouble(i -> i).sum();
                double gammaZedSum = IntStream.range(0, rules.size()).mapToDouble(i -> gammas.get(i) * zeds.get(i)).sum();

                System.out.println(calcualteError(trainSamples, rules));

                for (int i = 0; i < rules.size(); i++) {
                    Rule rule = rules.get(i);
                    double etaDiff = ETA * yDiff;
                    double alphai = alphas.get(i);
                    double betai = betas.get(i);
                    double gammai = gammas.get(i);
                    double abcdSame = etaDiff * (gammai * zeds.get(i) * rules.size() - gammaZedSum) / (gammaSum * gammaSum);
                    if (Double.isNaN(abcdSame)) {
                        x = x;
                    }

                    double da = abcdSame * betai * alphai * (1 - alphai) * rule.b;
                    double db = abcdSame * betai * alphai * (1 - alphai) * -(x - rule.a);
                    double dc = abcdSame * alphai * betai * (1 - betai) * rule.d;
                    double dd = abcdSame * alphai * betai * (1 - betai) * -(y - rule.c);

                    double pqrSame = etaDiff * gammai / gammaSum;

                    double dp = pqrSame * x;
                    double dq = pqrSame * y;
                    double dr = pqrSame;


                    rule.p += dp;
                    rule.q += dq;
                    rule.r += dr;

                    rule.a += da;
                    rule.b += db;
                    rule.c += dc;
                    rule.d += dd;

                    System.out.println(rule);
                }
                break;
            }

//            for (int i = 0; i < rules.size(); i++) {
//                Rule rule = rules.get(i);
//                Rule fakeRule = fakeRules.get(i);
//                int size = trainSamples.size();
//                fakeRule.a /= size;
//                fakeRule.b /= size;
//                fakeRule.c /= size;
//                fakeRule.d /= size;
//                fakeRule.p /= size;
//                fakeRule.q /= size;
//                fakeRule.r /= size;
//                rule.add(fakeRule);
//            }
            break;
        }

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
        double v2 = b * (x - a);
        double myexp = myexp(v2);
        double v1 = 1 + myexp;
        double v = 1 / v1;
        if (Double.isNaN(v)) {
            x = x;
        }
        return v;
    }

    private static double myexp(double v) {
        return Math.exp(v);
    }

    private static double calculateZ(Input input, Rule rule) {
        return rule.p * input.x + rule.q * input.y + rule.r;
    }


    private static double realOutput(double x, double y) {
        double cos = Math.cos(x / 5);
//        return ((x - 1) * (x - 1) + (y + 2) * (y + 2) - 5 * x * y + 3) * cos * cos;
        return 2 * x + y;
    }
}
