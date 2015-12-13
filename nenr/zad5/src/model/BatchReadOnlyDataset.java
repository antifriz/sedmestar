package model;

import ann.IReadOnlyDataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
    public Iterator<double[][]> iterator() {
        if (mSplit == 1) {
            return Collections.unmodifiableList(mRawDataset).iterator();
        }
        int from = (int) Math.ceil(mStep * mRawDataset.size() / (float) mSplit);
        int to = (int) Math.ceil(Math.min((mStep + 1) * mRawDataset.size() / (float) mSplit, mRawDataset.size()));

        return Collections.unmodifiableList(mRawDataset.subList(from, to)).iterator();
    }
}
