package hr.fer.zemris.ropaeruj.dz7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by ivan on 11/16/15.
 */
public class ParseableReadOnlyDataset implements IReadOnlyDataset {
    private final int mSize;
    private final int mInputDimension;
    private final int mOutputDimension;
    private double[][][] mData;

    private ParseableReadOnlyDataset(double[][][] data) {
        assert data.length > 0;
        assert data[0].length == 2;
        assert data[0][0].length > 0;
        assert data[0][1].length > 0;

        mSize = data.length;
        mInputDimension = data[0][0].length;
        mOutputDimension = data[0][1].length;
        mData = data;
    }

    public static ParseableReadOnlyDataset createFromFile(String filePath) throws IOException {
        double[][][] dataset = Files.lines(Paths.get(filePath)).map(x -> Arrays.stream(x.trim().replaceAll("[()]", "").split(":")).map(y -> Arrays.stream(y.split(",")).mapToDouble(Double::valueOf).toArray()).toArray(double[][]::new)).toArray(double[][][]::new);
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
        return mData[idx];
    }
}
