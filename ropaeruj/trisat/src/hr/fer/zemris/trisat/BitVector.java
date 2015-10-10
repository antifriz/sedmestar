package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by ivan on 10/9/15.
 */
public class BitVector {
    protected boolean[] mUnderlayingArray;

    public BitVector(Random rand, int numberOfBits) {
        this(numberOfBits);
        for (int i = numberOfBits - 1; i >= 0; i--) {
            mUnderlayingArray[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean... bits) {
        mUnderlayingArray = Arrays.copyOf(bits, bits.length);
    }

    public BitVector(int n) {
        mUnderlayingArray = new boolean[n];
    }

    public boolean get(int index) {
        return mUnderlayingArray[index];
    }

    public int getSize() {
        return mUnderlayingArray.length;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (boolean b :
                mUnderlayingArray) {
            stringBuilder.append(b ? "1" : "0");
        }

        return stringBuilder.toString();
    }

    public MutableBitVector copy() {
        return new MutableBitVector(mUnderlayingArray);
    }
}
