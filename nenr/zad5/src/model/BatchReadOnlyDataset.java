package model;

import ann.IReadOnlyDataset;

import java.util.*;

/**
 * Created by ivan on 12/13/15.
 */
public class BatchReadOnlyDataset implements IReadOnlyDataset {

    private final List<double[][]> mRawDataset;
    private final int mSplit;
    private int mStep = 0;

    public BatchReadOnlyDataset(List<double[][]> rawDataset, int split) {
        assert split != 0;
        if (split == -1) {
            split = rawDataset.size();
        }
        mSplit = split;
        mRawDataset = new ArrayList<>(rawDataset);
        Collections.shuffle(mRawDataset,new Random(69));
    }

    @Override
    public int getInputDimension() {
        return mRawDataset.get(0)[0].length;
    }

    @Override
    public int getOutputDimension() {
        return mRawDataset.get(0)[1].length;
    }

    @Override
    public void reset() {
        mStep = 0;
    }

    @Override
    public void next() {
        mStep++;
        mStep%=mSplit;
    }

    @Override
    public List<double[][]> getWhole() {
        return Collections.unmodifiableList(mRawDataset);
    }

    @Override
    public int getSize() {
        return getLimit(mStep +1) - getLimit(mStep);
    }

    @Override
    public Iterator<double[][]> iterator() {
        if (mSplit == 1) {
            return Collections.unmodifiableList(mRawDataset).iterator();
        }
        int from = getLimit(mStep);
        int to = getLimit(mStep+1);

        return Collections.unmodifiableList(mRawDataset.subList(from, to)).iterator();
    }

    private int getLimit(int step) {
        return (int) Math.ceil(Math.min(step * mRawDataset.size() / (float) mSplit, mRawDataset.size()));
    }
}
