package hr.fer.zemris.optjava.libopti;

import java.text.DecimalFormat;

/**
 * Created by ivan on 10/22/15.
 */
public interface IDecoder<T extends SingleObjectiveSolution> {
    double[] decode(T object);

    void decode(T object, double[] decodedArray);

    default String toString(T object) {
        double[] a = decode(object);
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(df.format(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
