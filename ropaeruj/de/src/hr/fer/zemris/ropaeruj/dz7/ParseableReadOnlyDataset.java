package hr.fer.zemris.ropaeruj.dz7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
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

    public static ParseableReadOnlyDataset createFromFile(String filePath) throws IOException {
        List<double[][]> dataset = Files.lines(Paths.get(filePath)).map(x -> Arrays.stream(x.trim().replaceAll("[()]", "").split(":")).map(y -> Arrays.stream(y.split(",")).mapToDouble(Double::valueOf).toArray()).toArray(double[][]::new)).collect(Collectors.toList());
        return new ParseableReadOnlyDataset(dataset);
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
