package model;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ivan on 12/13/15.
 */
public class CharacterObject {

    final double[] mXes;
    final double[] mYes;

    private final HashMap<Integer, double[]> mCache;
    private final Clazz mClazz;
    private File mFile;

    public CharacterObject(Clazz clazz, int[] xes, int[] yes) {
        assert xes.length == yes.length;

        mCache = new HashMap<>();

        mClazz = clazz;


        double averageX = Arrays.stream(xes).average().getAsDouble();
        double averageY = Arrays.stream(yes).average().getAsDouble();


        double[] xs = Arrays.stream(xes).mapToDouble(x -> (x - averageX)).toArray();
        double[] ys = Arrays.stream(yes).mapToDouble(y -> (y - averageY)).toArray();

        double minX = Arrays.stream(xs).min().getAsDouble();
        double maxX = Arrays.stream(xs).max().getAsDouble();

        double diffX = (maxX - minX);

        double minY = Arrays.stream(ys).min().getAsDouble();
        double maxY = Arrays.stream(ys).max().getAsDouble();

        double diffY = (maxY - minY);

        double diff = Math.max(diffX, diffY);

        mXes = Arrays.stream(xs).map(x -> x / diff * 2).toArray();
        mYes = Arrays.stream(ys).map(y -> y / diff * 2).toArray();
    }

    private CharacterObject(Clazz clazz, double[] xes, double[] yes) {
        assert xes.length == yes.length;
        mCache = new HashMap<>();

        mClazz = clazz;
        mXes = xes;
        mYes = yes;
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
                    yesReduced[i] = mXes[mYes.length - 1];
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
                    if (Double.isNaN(xesReduced[i])) {
                        xesReduced[i] = 0;
                    }
                    if (Double.isNaN(yesReduced[i])) {
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

    public static CharacterObject fromFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());

        try {
            Clazz clazz = Clazz.valueOf(lines.get(0).trim());
            double xes[] = Arrays.stream(lines.get(1).trim().split(",")).mapToDouble(Double::valueOf).toArray();
            double yes[] = Arrays.stream(lines.get(2).trim().split(",")).mapToDouble(Double::valueOf).toArray();
            return new CharacterObject(clazz, xes, yes);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new IOException(file.getName() + ": " + e.toString());
        }
    }

    public void toFile(File file) throws IOException {
        mFile = file;
        List<String> lines = new ArrayList<>();
        lines.add(mClazz.name());
        lines.add(String.join(",", Arrays.stream(mXes).mapToObj(Double::toString).collect(Collectors.toList())));
        lines.add(String.join(",", Arrays.stream(mYes).mapToObj(Double::toString).collect(Collectors.toList())));

        Files.write(file.toPath(), lines, Charset.defaultCharset());
    }

    public void deleteFromDisk() {
        if (mFile != null && !mFile.delete()) {
            mFile.deleteOnExit();
        }
    }
}
