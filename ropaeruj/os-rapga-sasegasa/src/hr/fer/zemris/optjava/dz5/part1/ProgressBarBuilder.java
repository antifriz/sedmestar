package hr.fer.zemris.optjava.dz5.part1;

/**
 * Created by ivan on 11/6/15.
 */
public class ProgressBarBuilder {
    public static String progress(double value, double limit, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        int threshold = (int) Math.round(length * (value / limit));
        for (int i = 0; i < threshold; i++) {
            stringBuilder.append('█');
        }
        for (int i = threshold; i < length; i++) {
            stringBuilder.append('▒');
        }
        return stringBuilder.toString();
    }
}
