package model;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ivan on 12/13/15.
 */
public class CharacterObject {

    private final double[] mXes;
    private final double[] mYes;

    private final HashMap<Integer, double[]> mCache;
    private final Clazz mClazz;

    public CharacterObject(Clazz clazz, int[] xes, int[] yes) {
        assert xes.length == yes.length;

        mCache = new HashMap<>();

        mClazz = clazz;

        int minX = Arrays.stream(xes).min().getAsInt();
        int maxX = Arrays.stream(xes).max().getAsInt();

        double averageX = Arrays.stream(xes).average().getAsDouble();
        double diffX = (maxX - minX);

        int minY = Arrays.stream(xes).min().getAsInt();
        int maxY = Arrays.stream(xes).max().getAsInt();

        double averageY = Arrays.stream(xes).average().getAsDouble();
        double diffY = (maxY - minY);

        double diff = Math.max(diffX, diffY);
        mXes = Arrays.stream(xes).mapToDouble(x -> (x - averageX) / diff * 2).toArray();
        mYes = Arrays.stream(yes).mapToDouble(y -> (y - averageY) / diff * 2).toArray();
    }

    public double[] getFeatures(int featureSize) {
        assert featureSize % 2 == 0;
        return mCache.computeIfAbsent(featureSize / 2, M -> {
            double[] distances = new double[mXes.length];
            distances[0] = 0;
            for (int i = 1; i < distances.length; i++) {
                distances[i] = distances[i - 1] + Point.distance(mXes[i], mYes[i], mXes[i - 1], mYes[i - 1]);
            }

            double D = distances[distances.length - 1];

            double[] xesReduced = new double[M];
            double[] yesReduced = new double[M];

            double step = D / (M - 1);
            int pointIdx = 0;
            for (int i = 0; i < M; i++) {
                double desiredDistance = step * i;
                if (desiredDistance >= D) {
                    xesReduced[i] = mXes[mXes.length - 1];
                    xesReduced[i] = mXes[mYes.length - 1];
                } else {
                    double realIdx;
                    while (distances[pointIdx] < desiredDistance) pointIdx++;
                    if (pointIdx == 0) {
                        realIdx = 0;
                    } else {
                        realIdx = pointIdx + (pointIdx < distances.length ? (desiredDistance - distances[pointIdx - 1]) / (distances[pointIdx] - distances[pointIdx - 1]) : 0);
                    }
                    int floorIdx = (int) Math.floor(realIdx);
                    double decimalPart = realIdx - floorIdx;

                    xesReduced[i] = mXes[floorIdx] + (floorIdx + 1 < distances.length ? decimalPart * (mXes[floorIdx + 1] - mXes[floorIdx]) : 0);
                    yesReduced[i] = mYes[floorIdx] + (floorIdx + 1 < distances.length ? decimalPart * (mYes[floorIdx + 1] - mYes[floorIdx]) : 0);
                    if(Double.isNaN(xesReduced[i])){
                        xesReduced[i] = 0;
                    }
                    if(Double.isNaN(yesReduced[i])){
                        yesReduced[i] = 0;
                    }
                }
            }

            double[] features = new double[M * 2];
            System.arraycopy(xesReduced, 0, features, 0, M);
            System.arraycopy(yesReduced, 0, features, M, M);

            return features;
        });
    }

    public int getClazzId() {
        return mClazz.ordinal();
    }

}
