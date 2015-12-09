package hr.fer.zemris.ropaeruj.dz8;

import hr.fer.zemris.ropaeruj.dz7.IReadOnlyDataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by ivan on 11/16/15.
 */
public class ParseableReadOnlyDataset implements IReadOnlyDataset {
    private final int mSize;
    private final int mInputDimension;
    private final int mOutputDimension;
    private List<double[][]> mData;

    private ParseableReadOnlyDataset(List<double[][]> data) {
        assert data.size() > 0;
        assert data.get(0).length == 2;
        assert data.get(0)[0].length > 0;
        assert data.get(0)[1].length > 0;

        mSize = data.size();
        mInputDimension = data.get(0)[0].length;
        mOutputDimension = data.get(0)[1].length;
        mData = Collections.unmodifiableList(data);
    }

    public static ParseableReadOnlyDataset loadData(String filePath, int l, int limit) throws IOException {
        double[] dataset = Files.lines(Paths.get(filePath)).mapToDouble(Double::valueOf).toArray();

        double max = Arrays.stream(dataset).max().getAsDouble();
        double min = Arrays.stream(dataset).min().getAsDouble();

        dataset = Arrays.stream(dataset).map(x->min+(max-min)*x).toArray();

        List<double[][]> data = new ArrayList<>();

        for (int i = 0; i < dataset.length - l; i++) {
            double[][] d = new double[2][];
            d[0] = new double[l];
            d[1] = new double[1];
            for (int j = 0; j < l; j++) {
                d[0][j] = dataset[i + j];
            }
            d[1][0] = dataset[i + l];

            data.add(d);
        }

        if (limit != -1 && limit < data.size()) {
            return new ParseableReadOnlyDataset(data.subList(0, limit));
        }
        return new ParseableReadOnlyDataset(data);
    }

    @Override
    public int getSize() {
        return mSize;
    }

    @Override
    public int getInputDimension() {
        return mInputDimension;
    }

    @Override
    public int getOutputDimension() {
        return mOutputDimension;
    }

    @Override
    public double[][] getSampleAt(int idx) {
        assert idx < mSize;
        return mData.get(idx);
    }

    @Override
    public Stream<double[][]> stream() {
        return mData.stream();
    }

    @Override
    public Iterator<double[][]> iterator() {
        return mData.iterator();
    }
}
